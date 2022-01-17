import sys
import time

import pandas as pd
import numpy as np
import shutil
import os
from pathlib import Path
from datetime import timedelta, date

from keras import models
from keras.models import Sequential
from keras.layers import LSTM, Dense
from keras.constraints import nonneg
from sklearn.preprocessing import MinMaxScaler

from common.utils import get_city_attr

maxcovid = 100000
mincovid = 0
maxhousing = 50000
minhousing = 0
maxemotion = 200
minemotion = 0
maxlen = 365
trainlen = 30
predictlen = 30
rootpath = Path(sys.path[0]).parent
modelpath = os.path.join(rootpath, 'models', 'housing')


# citymap = get_citymap()
# housingpath = os.path.join(rootpath, 'static', 'data', 'housing', 'history')


def trainhousing(city, db_conn):
    city_props = get_city_attr()
    attr = city_props[city]
    sql = "SELECT date FROM housing_count_comp WHERE city=%s AND state=%s AND country=%s ORDER BY date_int DESC LIMIT 1"
    cursor = db_conn.cursor()
    cursor.execute(sql, (attr[3], attr[4], attr[5]))
    result = cursor.fetchone()
    last_date = result[0]
    today = date.today()
    gap_len = (today - last_date.date()).days
    start_date = last_date - timedelta(maxlen - 1)
    start_date_int = int(time.mktime(start_date.timetuple()))
    last_date_int = int(time.mktime(last_date.timetuple()))
    sql = "SELECT median_price FROM housing_count_comp WHERE city=%s AND state=%s AND country=%s AND date_int " \
          "BETWEEN %s AND %s ORDER BY date_int ASC"
    housing_data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], start_date_int, last_date_int))
    sql = "SELECT updatetime AS date, newcase AS new FROM covid WHERE city=%s AND state=%s AND country=%s AND " \
          "updatetimeint BETWEEN %s AND %s ORDER BY updatetimeint ASC"
    data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], start_date_int, last_date_int))
    data['housing'] = housing_data['median_price']

    # data = query_data_with_length(citymap[city][0], citymap[city][1], citymap[city][2], maxlen)
    # housingfilepath = os.path.join(housingpath, 'housing_price_{}.csv'.format(city))
    # scorefilepath = os.path.join(housingpath, 'score_{}.csv'.format(city))
    # housingdata = pd.read_csv(housingfilepath, header=0)
    # scoredata = pd.read_csv(scorefilepath, header=0)
    length = len(data)
    # shiftlen = 30
    yaxisname = 'new'
    xaxisname = 'date'
    # date = [date for date in pandasdata['date']]
    # df = pd.DataFrame(zip(date, pandasdata[yaxisname].values),
    #                 columns=[xaxisname, yaxisname])
    # df.index = df[xaxisname]

    # df = df.sort_index(ascending=True, axis=0)
    # data = pd.DataFrame(index=range(0, len(df)), columns=[xaxisname, yaxisname])
    # for i in range(0, len(data)):
    #     data[xaxisname][i] = df[xaxisname][i]
    #     data[yaxisname][i] = df[yaxisname][i]
    # print(data.head())

    # scaler = MinMaxScaler(feature_range=(0, 1))
    # data['housing'] = housingdata['housing']
    # data['emotion'] = scoredata['Score']
    data.index = data.date
    data.drop(xaxisname, axis=1, inplace=True)
    train_data = data.values
    train_data = np.append(train_data, [[mincovid, minhousing], [maxcovid, maxhousing]], axis=0)
    # print(train_data)
    # train_data = final_data[0:length - predictlen, :]
    # valid_data=final_data[length-predictlen:,:]
    scaler = MinMaxScaler(feature_range=(0, 1))
    scaled_data = scaler.fit_transform(train_data)[:-2]

    # model_data = scaled_data[-trainlen:]
    # model_data = [model_data]
    # model_data = np.asarray(model_data)
    # model_data = np.reshape(model_data, (model_data.shape[0], model_data.shape[1], 3))

    # model_data = model_data.reshape(-1, 1)
    # model_data = scaler.transform(model_data)

    # # X_test = [model_data]
    # # X_test = np.array(X_test)
    # # X_test = np.reshape(X_test, (X_test.shape[0], X_test.shape[1], 1))

    for sl in range(1, predictlen + 1):
        total_gl = gap_len + sl - 1
        x_train_data, y_train_data = [], []
        for i in range(0, length - trainlen - total_gl):
            x_train_data.append(scaled_data[i:i + trainlen, 0:2])
            y_train_data.append(scaled_data[i + trainlen + total_gl - 1, 1])
        x_train_data = np.asarray(x_train_data)
        y_train_data = np.asarray(y_train_data)
        x_train_data = np.reshape(x_train_data, (x_train_data.shape[0], x_train_data.shape[1], 2))
        # print(x_train_data)

        lstm_model = Sequential()
        lstm_model.add(
            LSTM(units=50,
                 return_sequences=True,
                 input_shape=(np.shape(x_train_data)[1], 2)))
        lstm_model.add(LSTM(units=50))
        lstm_model.add(Dense(1, kernel_constraint=nonneg()))
        # print(model_data)
        # print('len of model_data: {}'.format(len(model_data)))

        lstm_model.compile(loss='mean_squared_error', optimizer='adam')
        # print(x_train_data)
        # x_train_data = np.array(x_train_data)
        # y_train_data = np.array(y_train_data)
        # print('--------------------------')
        # print(y_train_data)
        for i in range(1):
            lstm_model.fit(x_train_data,
                           y_train_data,
                           epochs=5,
                           batch_size=1,
                           verbose=2)

        # predicted = lstm_model.predict(model_data)
        # predicted = [np.append(predicted[0],[0,0])]
        # predicted = np.asarray(predicted)
        # predicted = scaler.inverse_transform(predicted)
        # print(predicted)

        localmodelpath = os.path.join(modelpath, 'city_{}'.format(city), 'predictlen_{}'.format(sl))
        if os.path.exists(localmodelpath):
            for file in os.listdir(localmodelpath):
                filepath = os.path.join(localmodelpath, file)
                if os.path.isfile(filepath):
                    os.remove(filepath)
                else:
                    shutil.rmtree(filepath)
        else:
            os.makedirs(localmodelpath)
        lstm_model.save(os.path.join(localmodelpath, 'model.h5'))


def predicthousing(city, db_conn, engine):
    city_props = get_city_attr()
    attr = city_props[city]
    sql = "SELECT date FROM housing_count_comp WHERE city=%s AND state=%s AND country=%s ORDER BY date_int DESC LIMIT 1"
    cursor = db_conn.cursor()
    cursor.execute(sql, (attr[3], attr[4], attr[5]))
    result = cursor.fetchone()
    last_date = result[0]
    today = date.today()
    gap_len = (today - last_date.date()).days
    train_start_date = last_date - timedelta(trainlen - 1)
    train_start_date_int = int(time.mktime(train_start_date.timetuple()))
    last_date_int = int(time.mktime(last_date.timetuple()))
    sql = "SELECT median_price FROM housing_count_comp WHERE city=%s AND state=%s AND country=%s AND date_int " \
          "BETWEEN %s AND %s ORDER BY date_int ASC"
    housing_data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], train_start_date_int, last_date_int))
    sql = "SELECT updatetime AS date, newcase AS new FROM covid WHERE city=%s AND state=%s AND country=%s AND " \
          "updatetimeint BETWEEN %s AND %s ORDER BY updatetimeint ASC"
    data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], train_start_date_int, last_date_int))
    data['housing'] = housing_data['median_price']

    # data = query_data_with_length(citymap[city][0], citymap[city][1], citymap[city][2], trainlen)
    # housingfilepath = os.path.join(housingpath, 'housing_price_{}.csv'.format(city))
    # scorefilepath = os.path.join(housingpath, 'score_{}.csv'.format(city))
    # housingdata = pd.read_csv(housingfilepath, header=0)[-trainlen:]
    # scoredata = pd.read_csv(scorefilepath, header=0)[-trainlen:]
    # housingdata.index = data.index
    # scoredata.index = data.index
    # print(housingdata)
    # length = len(data)
    yaxisname = 'new'
    xaxisname = 'date'
    # td = datetime.now()
    predicted_arr = []
    for i in range(gap_len, gap_len + predictlen):
        aim_date = last_date + timedelta(days=i)
        predicted_arr.append([attr[3], attr[4], attr[5], aim_date, int(time.mktime(aim_date.timetuple()))])
    predict_data = pd.DataFrame(predicted_arr,
                                columns=['city', 'state', 'country', 'date', 'date_int'])
    # predict_data.index = predict_data.date
    # predict_data.drop(xaxisname, axis=1, inplace=True)
    # date = [date for date in pandasdata['date']]
    # df = pd.DataFrame(zip(date, pandasdata[yaxisname].values),
    #                 columns=[xaxisname, yaxisname])
    # df.index = df[xaxisname]

    # df = df.sort_index(ascending=True, axis=0)
    # data = pd.DataFrame(index=range(0, len(df)), columns=[xaxisname, yaxisname])
    # for i in range(0, len(data)):
    #     data[xaxisname][i] = df[xaxisname][i]
    #     data[yaxisname][i] = df[yaxisname][i]
    # print(data.head())

    # scaler = MinMaxScaler(feature_range=(0, 1))
    # data['housing'] = housingdata['housing']
    # data['emotion'] = scoredata['Score']
    data.index = data.date
    data.drop(xaxisname, axis=1, inplace=True)
    test_data = data.values
    test_data = np.append(test_data, [[mincovid, minhousing], [maxcovid, maxhousing]], axis=0)
    scaler = MinMaxScaler(feature_range=(0, 1))
    model_data = scaler.fit_transform(test_data)[:-2]

    X_test = [model_data]
    X_test = np.array(X_test)
    X_test = np.reshape(X_test, (X_test.shape[0], X_test.shape[1], 2))

    predicted_res = []
    for sl in range(1, predictlen + 1):
        # total_gl = gap_len + sl - 1
        localmodelpath = os.path.join(modelpath, 'city_{}'.format(city), 'predictlen_{}'.format(sl), 'model.h5')
        lstm_model = models.load_model(localmodelpath)

        predicted = lstm_model.predict(X_test)
        predicted = np.insert(predicted, 0, 0, axis=1)
        # predicted = np.insert(predicted, 2, 0, axis=1)
        # predicted = [np.append(predicted[0],[0,0])]
        # predicted = np.asarray(predicted)
        predicted = scaler.inverse_transform(predicted)
        predicted_res.append(0 if predicted[0][1] < 0 else round(predicted[0][1], 2))

    predict_data['predicted_median_price'] = predicted_res
    # sql = "DELETE FROM housing_median_prediction"
    # cursor = db_conn.cursor()
    # cursor.execute(sql)
    # db_conn.commit()
    predict_data.to_sql(con=engine, name='housing_median_prediction', if_exists='append', index=False)
    # savepath = os.path.join(rootpath, 'static', 'data', 'housing', 'predict')
    # if not os.path.exists(savepath):
    #     os.makedirs(savepath)
    # savefilepath = os.path.join(savepath, 'city_{}.csv'.format(city))
    # predict_data.to_csv(savefilepath, mode='w')


# credentials = Properties('../resources/credentials.properties').get_properties()
# user = credentials['user']
# pw = credentials['pw']
# address = credentials['address']
# db = credentials['db']
# db_conn = mysql.connector.connect(
#     host=address,
#     user=user,
#     password=pw,
#     database=db,
# )
# engine = sqlalchemy.create_engine("mysql+pymysql://{user}:{pw}@{address}/{db}"
#                                   .format(user=user,
#                                           pw=pw,
#                                           address=address,
#                                           db=db))
# config = Config.get_instance()
# valid_cities = config['housing_valid_cities'].split('/')
# # for city in valid_cities:
# #     trainhousing(city, db_conn)
# sql = "DELETE FROM housing_median_prediction"
# cursor = db_conn.cursor()
# cursor.execute(sql)
# db_conn.commit()
# for city in valid_cities:
#     predicthousing(city, db_conn, engine)
# # city = 'nyc'
# # attr = city_props[city]
# # # trainhousing(city, db_conn)
# # predicthousing(city, db_conn, engine)
# db_conn.close()



# sql = "SELECT date FROM housing_count_comp WHERE city=%s AND state=%s AND country=%s ORDER BY date_int DESC LIMIT 1"
# cursor = db_conn.cursor()
# cursor.execute(sql, (attr[3], attr[4], attr[5]))
# result = cursor.fetchone()
# last_date = result[0]
# print("last_date: ", last_date)
# today = date.today()
# gap_len = (today - last_date.date()).days
# train_start_date = last_date - timedelta(trainlen - 1)
# train_start_date_int = int(time.mktime(train_start_date.timetuple()))
# last_date_int = int(time.mktime(last_date.timetuple()))
# print("last_date_int: ", last_date_int)
# sql = "SELECT date, date_int, median_price FROM housing_count_comp WHERE city=%s AND state=%s AND country=%s AND " \
#       "date_int " \
#       "BETWEEN %s AND %s ORDER BY date_int ASC"
# data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], train_start_date_int, last_date_int))
# sql = "SELECT newcase AS new FROM covid WHERE city=%s AND state=%s AND country=%s AND " \
#       "updatetimeint BETWEEN %s AND %s ORDER BY updatetimeint ASC"
# covid_data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], train_start_date_int, last_date_int))
# data['new'] = covid_data['new']
# print(data)
# db_conn.close()
