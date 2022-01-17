import datetime
import os
import shutil
import sys
from pathlib import Path
import time
from datetime import datetime as dt, timedelta
import pandas as pd
import numpy as np
from keras import models
from keras.models import Sequential
from keras.layers import LSTM, Dense
from keras.constraints import nonneg
from sklearn.preprocessing import MinMaxScaler
from common.utils import get_city_attr

max = 100000
min = 0
maxlen = 365
trainlen = 14
predictlen = 30
rootpath = Path(sys.path[0]).parent
modelpath = os.path.join(rootpath, 'models', 'covid')


def traincovid(city, db_conn):
    city_props = get_city_attr()
    attr = city_props[city]
    date_end = dt.now()
    date_start = date_end - timedelta(maxlen)
    date_end_int = int(time.mktime(date_end.timetuple()))
    date_start_int = int(time.mktime(date_start.timetuple()))
    sql = "SELECT updatetime AS date, newcase AS new FROM covid WHERE city=%s AND state=%s AND country=%s AND " \
          "updatetimeint BETWEEN %s AND %s ORDER BY updatetimeint ASC"
    data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], date_start_int, date_end_int))
    # data = query_data_with_length(citymap[city][0], citymap[city][1], citymap[city][2], maxlen)
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
    data.index = data.date
    data.drop(xaxisname, axis=1, inplace=True)
    train_data = data.values
    train_data = np.append(train_data, [[min], [max]], axis=0)
    # train_data = final_data[0:length - predictlen, :]
    # valid_data=final_data[length-predictlen:,:]
    scaler = MinMaxScaler(feature_range=(0, 1))
    scaled_data = scaler.fit_transform(train_data)[:-2]

    # model_data = data[-trainlen:].values
    # model_data = model_data.reshape(-1, 1)
    # model_data = scaler.transform(model_data)

    # X_test = [model_data]
    # X_test = np.array(X_test)
    # X_test = np.reshape(X_test, (X_test.shape[0], X_test.shape[1], 1))

    for sl in range(1, predictlen + 1):
        x_train_data, y_train_data = [], []
        for i in range(0, length - trainlen - sl):
            x_train_data.append(scaled_data[i:i + trainlen, 0])
            y_train_data.append(scaled_data[i + trainlen + sl - 1, 0])
        x_train_data = np.asarray(x_train_data)
        y_train_data = np.asarray(y_train_data)
        x_train_data = np.reshape(
            x_train_data, (x_train_data.shape[0], x_train_data.shape[1], 1))

        lstm_model = Sequential()
        lstm_model.add(
            LSTM(units=50,
                 return_sequences=True,
                 input_shape=(np.shape(x_train_data)[1], 1)))
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

        # modelname = 'city_{}/predictlen_{}'.format(city,sl)
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


def predictcovid(city, db_conn, engine):
    city_props = get_city_attr()
    attr = city_props[city]
    sql = "SELECT * FROM (SELECT updatetime AS date, newcase AS new FROM covid WHERE city=%s AND state=%s AND " \
          "country=%s ORDER BY updatetimeint DESC LIMIT %s) AS t1 ORDER BY t1.date ASC"
    data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], trainlen))
    # length = len(data)
    # yaxisname = 'new'
    xaxisname = 'date'
    td = datetime.date.today()
    predicted_arr = []
    for i in range(predictlen):
        aim_date = td + timedelta(days=i)
        predicted_arr.append([attr[3], attr[4], attr[5], aim_date, int(time.mktime(aim_date.timetuple()))])
    predictdata = pd.DataFrame(predicted_arr,
                               columns=["city", "state", "country", "date", "date_int"])
    # predictdata.index = predictdata.date
    # predictdata.drop(xaxisname, axis=1, inplace=True)
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
    data.index = data.date
    data.drop(xaxisname, axis=1, inplace=True)
    test_data = data.values
    test_data = np.append(test_data, [[min], [max]], axis=0)
    scaler = MinMaxScaler(feature_range=(0, 1))
    model_data = scaler.fit_transform(test_data)[:-2]

    X_test = [model_data]
    X_test = np.array(X_test)
    X_test = np.reshape(X_test, (X_test.shape[0], X_test.shape[1], 1))

    predicted_res = []
    for sl in range(1, predictlen + 1):
        localmodelpath = os.path.join(modelpath, 'city_{}'.format(city), 'predictlen_{}'.format(sl), 'model.h5')
        # print("model: {}".format(localmodelpath))
        lstm_model = models.load_model(localmodelpath)

        predicted_stock_price = lstm_model.predict(X_test)
        predicted_stock_price = scaler.inverse_transform(predicted_stock_price)
        predicted_res.append(0 if predicted_stock_price[0][0] < 0 else round(predicted_stock_price[0][0], 0))

    predictdata['predicted_newcases'] = predicted_res
    # sql = "DELETE FROM covid_prediction"
    # cursor = db_conn.cursor()
    # cursor.execute(sql)
    # db_conn.commit()
    predictdata.to_sql(con=engine, name='covid_prediction', if_exists='append', index=False)
    # savepath = os.path.join(rootpath, 'static', 'data', 'covid', 'predict')
    # if not os.path.exists(savepath):
    #     os.makedirs(savepath)
    # savefilepath = os.path.join(savepath, 'city_{}.csv'.format(city))
    # predictdata.to_csv(savefilepath, mode='w')


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
# for city in valid_cities:
#     traincovid(city, db_conn)
# sql = "DELETE FROM covid_prediction"
# cursor = db_conn.cursor()
# cursor.execute(sql)
# db_conn.commit()
# for city in valid_cities:
#     predictcovid(city, db_conn, engine)
# db_conn.close()



# city_props = get_city_attr()
# attr = city_props[city]
# date_end = dt.now()
# date_start = date_end - timedelta(maxlen)
# date_end_int = int(time.mktime(date_end.timetuple()))
# date_start_int = int(time.mktime(date_start.timetuple()))
# sql = "SELECT updatetime AS date, newcase AS new FROM covid WHERE city=%s AND state=%s AND country=%s AND " \
#       "updatetimeint BETWEEN %s AND %s ORDER BY updatetimeint ASC"
# data = pd.read_sql(sql, db_conn, params=(attr[3], attr[4], attr[5], date_start_int, date_end_int))
# print(data)
