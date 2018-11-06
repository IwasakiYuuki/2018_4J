class CharDatas(object):
    def __init__(self, paths):
        self.paths = paths
        self.datas = self.__multi_load(paths)

    def __load_data(self, path):
        with open(path) as f:
            data = list(map(lambda x: int(x), f.readlines()))
        return data

    def __multi_load(self, paths):
        data = []
        for path in paths:
            lines = self.__load_data(path)
            data.append(lines)
        return data

    def get_mean(self):
        buf = []
        rev_datas = zip(*self.datas)
        length = self.datas.__len__()
        for data in rev_datas:
            buf.append(sum(data))
        buf = list(map(lambda x: x/length, buf))
        return buf


if __name__ == '__main__':
    paths = ['c'+'{:02}'.format(i)+'.txt' for i in range(1, 47)]
    paths = map(lambda x: '../char/'+x, paths)
    char = CharDatas(paths)
    char.get_mean()
