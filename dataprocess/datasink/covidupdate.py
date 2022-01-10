from common.utils import Config, Properties
from datetime import datetime as dt, timedelta
import pandas
import time
import sqlalchemy
import os


def parse_city(city_str):
    res = {}
    city_str = city_str[1:len(city_str) - 1]
    cc = city_str.split('),(')
    for tp in cc:
        tps = tp.split(',')
        nest = [attr.strip() for attr in tps]
        res[nest[0]] = nest
    return res


def covid_single_update(date_str):
    credentials = Properties('../resources/credentials.properties').get_properties()
    user = credentials['user']
    pw = credentials['pw']
    address = credentials['address']
    db = credentials['db']
    prop = Config.get_instance()
    table = prop['covid_dest_table']
    source_path_prefix = prop['covid_source_prefix']
    time_format = '%m-%d-%Y'
    source_suffix = '.csv'
    df_column = ['city', 'state', 'country', 'confirmed', 'newcase', 'updatetime', 'updatetimeint']
    city_str = prop['valid_cities']
    valid_cities = parse_city(city_str)
    date = dt.strptime(date_str, time_format)
    table_time_format = '%Y-%m-%d %H:%M:%S'
    data_list = []

    engine = sqlalchemy.create_engine("mysql+pymysql://{user}:{pw}@{address}/{db}"
                                      .format(user=user,
                                              pw=pw,
                                              address=address,
                                              db=db))

    prev_date_str = (date - timedelta(1)).strftime(table_time_format)
    SQL = "SELECT C.city, C.confirmed FROM " + table + " C WHERE C.updatetime='" + prev_date_str + "'"
    prev_df = pandas.read_sql(SQL, engine)
    prev_confirmed_dict = {}
    for index, row in prev_df.iterrows():
        prev_confirmed_dict[row['city']] = int(row['confirmed'])

    filename = date_str + source_suffix
    path = os.path.join(source_path_prefix, filename)
    df_raw = pandas.read_csv(filepath_or_buffer=path, header=0)

    row_idx = 0
    for index, row in df_raw.iterrows():
        if pandas.isna(row['Admin2']):
            city_name = row['Province_State']
        else:
            city_name = row['Admin2']
        if [city_name, row['Province_State'], row['Country_Region']] in valid_cities.values():
            data_list.append([])
            data_list[row_idx].append(city_name)
            data_list[row_idx].append(row['Province_State'])
            data_list[row_idx].append(row['Country_Region'])
            data_list[row_idx].append(int(row['Confirmed']))
            data_list[row_idx].append(
                int(row['Confirmed']) - prev_confirmed_dict[city_name])
            data_list[row_idx].append(dt.strptime(date_str, time_format))
            data_list[row_idx].append(int(time.mktime(data_list[row_idx][5].timetuple())))
            row_idx += 1

    df_sink = pandas.DataFrame(data_list, columns=df_column)

    # df_sink.to_sql(con=engine, name=table, if_exists='append', index=False)
    print(df_sink)


def covid_daily_update():
    time_format = '%m-%d-%Y'
    date_str = (dt.now() - timedelta(1)).strftime(time_format)
    covid_single_update(date_str)


covid_single_update('01-09-2022')

# dend = dt.strptime('2020-12-31', '%Y-%m-%d')
# dstart = dt.strptime('2021-12-09', '%Y-%m-%d')
# l = (dend-dstart).days+1
# for d in range(2):
# covid_single_update((dstart+timedelta(days=d)).strftime('%m-%d-%Y'))