#kadai1-1 s15023 ���I�I
import os, sys
import matplotlib

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
                f.write(str(line)+'?_n')


def multi_list(l1, l2):
    return [x*y for (x, y) in zip(l1, l2)]


class CharDatas(object):
    def __init__(self, datas):
        self.paths = paths
        self.datas = datas
        self.means = self.get_mean()

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

    def get_covariance(self):
        answers = []
        for num, data in enumerate(self.datas):
            rev_data = list(zip(*data))
            answer = []
            mean = means[num]
            for i in range(FEATURE):
                buf = [((sum(multi_list(rev_data[i], rev_data[j]))/CHARACTER) - mean[i]*mean[j]) for j in range(FEATURE)]
#                for j in range(FEATURE):
#                    buf.append((sum(multi_list(rev_data[i], rev_data[j]))/CHARACTER) - mean[i]*mean[j])
                answer.append(buf)
            answers.append(answer)
            print(num)
        return answers



if __name__ == '__main__':
    paths = list(map(lambda x: '../char/'+x, ['c'+'{:02}'.format(i)+'.txt' for i in range(1, FILES)]))
    char = CharDatas(multi_load(paths))
    means = char.get_mean()
    covs = char.get_covariance()
    for i, cov in enumerate(covs):
        print('--------------'+str(i)+'---------------\n')
        for column in cov:
            print(column)