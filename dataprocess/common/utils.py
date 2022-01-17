import datetime
import time


class Properties(object):
    def __init__(self, filename):
        self.filename = filename
        self.properties = {}

    def __get_dict(self, str_name, dict_name, value):
        if str_name.find('.') > 0:
            k = str_name.split('.')[0]
            dict_name.setdefault(k, {})
            return self.__get_dict(str_name[len(k) + 1:], dict_name[k], value)
        else:
            dict_name[str_name] = value
            return

    def get_properties(self):
        try:
            pro_file = open(self.filename, 'r')
            for line in pro_file.readlines():
                line = line.strip().replace('\n', '')
                if line.find('#') != -1:
                    line = line[0:line.find('#')]
                if line.find('=') > 0:
                    strs = line.split('=')
                    strs[1] = line[len(strs[0]) + 1:]
                    self.__get_dict(strs[0].strip(), self.properties, strs[1].strip())
        except Exception:
            print('Read properties error')
        else:
            pro_file.close()
        return self.properties


class Config(object):
    __instance = None

    def __init__(self) -> None:
        super().__init__()

    @classmethod
    def get_instance(cls):
        if not cls.__instance:
            cls.__instance = Properties(
                '../resources/config.properties'
            ).get_properties()
        return cls.__instance


def get_city_attr():
    city_props_raw = Properties('../resources/housing_retrieve_sink.properties').get_properties()
    city_props = {}
    for key, value in city_props_raw.items():
        city_props[key] = value.strip().split('/')
    return city_props
