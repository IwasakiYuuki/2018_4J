import os, sys

FEATURE = 196
CHARACTER = 180
FILES = 47


def load_data(path):
    datas = []
    with open(path) as f:
        data = list(map(lambda x: int(x), f.readlines()))
    for i in range(CHARACTER):
        datas.append(data[i*FEATURE:(i+1)*FEATURE])
    return datas


def multi_load(paths):
    data = []
    for path in paths:
        lines = load_data(path)
        data.append(lines)
    return data


def write_mean(means):
    paths = list(map(lambda x: '../mean/'+x, ['mean'+'{:02}'.format(i)+'.txt' for i in range(1, FILES)]))
    for (i, mean) in enumerate(means):
        with open(paths[i], 'w') as f:
            for line in mean:
                f.write(str(line)+'\n')


class CharDatas(object):
    def __init__(self, datas):
        self.paths = paths
        self.datas = datas

    def get_mean(self):
        bufs = []
        for data in self.datas:
            buf = []
            rev_data = zip(*data)
            for feature in rev_data:
                buf.append(sum(feature))
            buf = list(map(lambda x: x/CHARACTER, buf))
            bufs.append(buf)
        return bufs


if __name__ == '__main__':
    paths = list(map(lambda x: '../char/'+x, ['c'+'{:02}'.format(i)+'.txt' for i in range(1, FILES)]))
    print(paths)
    char = CharDatas(multi_load(paths))
    means = char.get_mean()
    write_mean(means)
