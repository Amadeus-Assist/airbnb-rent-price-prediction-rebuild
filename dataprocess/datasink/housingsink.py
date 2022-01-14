import datetime

import requests
from datetime import datetime as dt, timedelta, date
import mysql.connector
import pandas as pd
import numpy as np
import statistics
import time
import os

import sqlalchemy

from common.utils import Properties, Config


def get_room_ratios() -> dict:
    return {
        'Entire home/apt': 1.0,
        'Private room': 2.4956901481075344,
        'Hotel room': 0.8376719514122621,
        'Shared room': 2.720069258818556
    }


def download_housing_files(city_short: str, city: str, state: str, country: str, date: str) -> str:
    url = "http://data.insideairbnb.com/{}/{}/{}/{}/visualisations/listings.csv".format(country, state, city, date)
    print("request url: ", url)
    r = requests.get(url, stream=True)
    if r.ok:
        print("get OK response")
        filename = "../housing_raw_data/" + city_short + "_listings.csv"
        with open(filename, "wb") as file:
            for chunk in r.iter_content(chunk_size=1024):
                # writing one chunk at a time to pdf file
                if chunk:
                    file.write(chunk)
        return filename
    else:
        return ""


def process_raw_single_city(city: str, city_attr: list, filename: str, last_date):
    print("into function")
    raw_df = pd.read_csv(filename, dtype={'number_of_reviews_ltm': str, 'license': str})
    raw_df['last_review'] = pd.to_datetime(raw_df.last_review)
    print(raw_df)
    print(raw_df['last_review'])
    price_raw_arr = []
    room_ratios = get_room_ratios()
    price_count = {}
    for index, row in raw_df.iterrows():
        if (not row['last_review'] is pd.NaT) and row['last_review'].date() > last_date and row['room_type'] in \
                room_ratios:
            review_date = row['last_review']
            room_type = row['room_type']
            std_price = row['price'] * room_ratios[room_type]
            price_raw_arr.append([city_attr[3], city_attr[4], city_attr[5], std_price, review_date,
                                  int(time.mktime(last_date.timetuple()))])
            if review_date in price_count:
                price_count[review_date].append(std_price)
            else:
                price_count[review_date] = [std_price]
    price_df = pd.DataFrame(price_raw_arr, columns=['city', 'state', 'country', 'std_price', 'date', 'date_int'])

    price_count_arr = []
    for key, value in price_count.items():
        avg_price = statistics.mean(value)
        median_price = statistics.median(value)
        price_count_arr.append([city_attr[3], city_attr[4], city_attr[5], avg_price, median_price, key,
                                int(time.mktime(key.timetuple()))])
    price_count_df = pd.DataFrame(price_count_arr, columns=['city', 'state', 'country', 'avg_price', 'median_price',
                                                            'date', 'date_int'])
    print("price_df")
    print(price_df)
    print("price_count_df")
    print(price_count_df)
    return price_df, price_count_df


def complete_avg_first_time(price_count_df, city, state, country):
    sorted_df = price_count_df.sort_values(by=['date_int'])
    row_1 = sorted_df.iloc[0]
    last_date = row_1['date']
    last_price = row_1['avg_price']
    complete_arr = [[city, state, country, last_price, last_date,
                     int(time.mktime(last_date.timetuple()))]]
    sorted_df.drop(index=sorted_df.index[0],
                   axis=0,
                   inplace=True)
    complete_arr = complete_avg_basic(sorted_df, complete_arr, last_date, last_price, city, state, country)
    price_comp_df = pd.DataFrame(complete_arr, columns=['city', 'state', 'country', 'avg_price',
                                                        'date', 'date_int'])
    return price_comp_df


def complete_avg_append(price_count_df, last_date, last_price, city, state, country):
    sorted_df = price_count_df.sort_values(by=['date_int'])
    complete_arr = []
    complete_avg_basic(sorted_df, complete_arr, last_date, last_price, city, state, country)
    price_comp_df = pd.DataFrame(complete_arr, columns=['city', 'state', 'country', 'avg_price',
                                                        'date', 'date_int'])
    return price_comp_df


def complete_avg_basic(price_count_df_sorted, complete_arr, last_date, last_price, city, state, country):
    previous_date = last_date
    previous_price = last_price
    for index, row in price_count_df_sorted.iterrows():
        current_date = row['date']
        current_price = row['avg_price']
        diff_day = (current_date - previous_date).days
        slope = (current_price - previous_price) / diff_day
        for i in range(1, diff_day):
            insert_price = slope * i + previous_price
            insert_date = previous_date + timedelta(i)
            complete_arr.append([city, state, country, insert_price, insert_date,
                                 int(time.mktime(insert_date.timetuple()))])
        complete_arr.append([city, state, country, current_price, current_date,
                             int(time.mktime(current_date.timetuple()))])
        previous_date = current_date
        previous_price = current_price
    return complete_arr


def update_housing_single_city(city: str, city_props: dict, db_conn, engine, is_first: bool):
    date_format = "%Y-%m-%d"
    cursor = db_conn.cursor()
    sql = "SELECT date FROM housing_update_record WHERE city=%s"
    cursor.execute(sql, (city,))
    result = cursor.fetchall()
    print("Date Result: ", result)
    last_date = result[0][0]
    print("date_str: ", last_date)
    attr = city_props[city]
    # last_date = dt.strptime(last_date_str, date_format)
    cur_date = datetime.date.today()
    print("city_short: ", city)
    while cur_date > last_date:
        filename = download_housing_files(city, attr[0], attr[1], attr[2], cur_date.strftime(date_format))
        if len(filename):
            print("returned filename: ", filename)
            price_df, price_count_df = process_raw_single_city(city, city_props[city], filename, last_date)
            price_df.to_sql(con=engine, name='housing_raw', if_exists='append', index=False)
            price_count_df.to_sql(con=engine, name='housing_count', if_exists='append', index=False)
            if is_first:
                price_comp_df = complete_avg_first_time(price_count_df, attr[3], attr[4], attr[5])
            else:
                sql = "SELECT date, avg_price FROM housing_avg_comp WHERE city=%s AND state=%s AND country=%s ORDER " \
                      "BY date_int DESC LIMIT 1"
                cursor.execute(sql, (attr[3], attr[4], attr[5],))
                result = cursor.fetchall()
                last_comp_date = result[0][0]
                last_comp_price = result[0][1]
                price_comp_df = complete_avg_append(price_count_df, last_comp_date, last_comp_price, attr[3],
                                                    attr[4], attr[5])
            price_comp_df.to_sql(con=engine, name='housing_avg_comp', if_exists='append', index=False)
            sql = "UPDATE housing_update_record SET date=%s WHERE city=%s"
            val = (cur_date.strftime(date_format), city)
            cursor.execute(sql, val)
            db_conn.commit()
            break
        cur_date = cur_date - timedelta(1)


credentials = Properties('../resources/credentials.properties').get_properties()
user = credentials['user']
pw = credentials['pw']
address = credentials['address']
db = credentials['db']
db_conn = mysql.connector.connect(
    host=address,
    user=user,
    password=pw,
    database=db,
)
engine = sqlalchemy.create_engine("mysql+pymysql://{user}:{pw}@{address}/{db}"
                                  .format(user=user,
                                          pw=pw,
                                          address=address,
                                          db=db))
city_props_raw = Properties('../resources/housing_retrieve_sink.properties').get_properties()
city_props = {}
for key, value in city_props_raw.items():
    city_props[key] = value.strip().split('/')
config = Config.get_instance()
valid_cities = config['housing_valid_cities'].split('/')
city = 'twin-cities'
attr = city_props[city]
for city in valid_cities:
    update_housing_single_city(city, city_props, db_conn, engine, True)


# last_date = dt.strptime('2021-05-01', '%Y-%m-%d')
# city = 'twin-cities'
# attr = city_props[city]
# filename = "../housing_raw_data/{}_listings.csv".format(city)
# price_df, price_count_df = process_raw_single_city(city, city_props[city], filename, last_date)
# price_comp_df = complete_avg_first_time(price_count_df, attr[3], attr[4], attr[5])
# print(price_comp_df.head(50))

# date_now = datetime.date.today()
# print(date_now - timedelta(10))

# url = "http://data.insideairbnb.com/united-states/ny/new-york-city/2021-11-02/visualisations/listings.csv"

# r = requests.get(url, stream=True)
#
# print("response.ok: ", r.ok)
#
# with open("listings.csv", "wb") as pdf:
#     for chunk in r.iter_content(chunk_size=1024):
#
#         # writing one chunk at a time to pdf file
#         if chunk:
#             pdf.write(chunk)
