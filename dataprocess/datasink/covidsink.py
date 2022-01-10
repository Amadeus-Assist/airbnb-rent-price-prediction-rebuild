from common.utils import Config, Properties
from datetime import datetime as dt, timedelta
import pandas
import time
import sqlalchemy
import os


def parse_city(city_str):
    res = set()
    city_str = city_str[1:len(city_str) - 1]
    cc = city_str.split('),(')
    for tp in cc:
        tps = tp.split(',')
        res.add((tps[0].strip(), tps[1].strip(), tps[2].strip()))
    return res


prop = Config.get_instance()
credentials = Properties('../resources/credentials.properties').get_properties()
user = credentials['user']
pw = credentials['pw']
address = credentials['address']
db = credentials['db']
table = prop['covid_dest_table']
source_path_prefix = prop['covid_source_prefix']
data_range = prop['covid_date_range']
time_format = '%m-%d-%Y'
source_suffix = '.csv'
df_column = ['city', 'state', 'country', 'confirmed', 'newcase', 'updatetime', 'updatetimeint']
citystr = prop['valid_cities']
valid_cities = parse_city(citystr)
data_list = []

if data_range.find('/') != -1:
    sp = data_range.find('/')
    start_date = dt.strptime(data_range[0:sp].strip(), time_format)
    end_date = dt.strptime(data_range[sp + 1:].strip(), time_format)
else:
    today = dt.now().strftime(time_format)
    end_date = today - dt.timedelta(1)
    start_date = today - dt.timedelta(data_range)

days = (end_date - start_date).days + 1
row_idx = 0
for i in range(days):
    timestamp = start_date + timedelta(i)
    date_str = timestamp.strftime(time_format)
    filename = date_str + source_suffix
    path = os.path.join(source_path_prefix, filename)
    df_raw = pandas.read_csv(filepath_or_buffer=path, header=0)
    for index, row in df_raw.iterrows():
        if pandas.isna(row['Admin2']):
            city_name = row['Province_State']
        else:
            city_name = row['Admin2']
        if (city_name, row['Province_State'], row['Country_Region']) in valid_cities:
            data_list.append([])
            data_list[row_idx].append(city_name)
            data_list[row_idx].append(row['Province_State'])
            data_list[row_idx].append(row['Country_Region'])
            data_list[row_idx].append(int(row['Confirmed']))
            data_list[row_idx].append(0)
            if row_idx >= len(valid_cities):
                data_list[row_idx][4] = data_list[row_idx][3] - data_list[row_idx - len(valid_cities)][3]
                if data_list[row_idx][4] < 0:
                    data_list[row_idx][4] = 0
            data_list[row_idx].append(dt.strptime(date_str, time_format))
            data_list[row_idx].append(int(time.mktime(data_list[row_idx][5].timetuple())))
            row_idx += 1

df_sink = pandas.DataFrame(data_list, columns=df_column)
# df_sink['date'] = pandas.to_datetime(df_sink.date)

engine = sqlalchemy.create_engine("mysql+pymysql://{user}:{pw}@{address}/{db}"
                                  .format(user=user,
                                          pw=pw,
                                          address=address,
                                          db=db))

df_sink.to_sql(con=engine, name=table, if_exists='append', index=False)
