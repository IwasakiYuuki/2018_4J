# kadai1-2 s15023 岩崎悠紀
import math
import numpy as np
import time

FEATURE = 196
CHARACTER = 180
FILES = 47


def load_data(path):
    datas = []
    with open(path) as f:
        data = list(map(lambda x: int(x), f.readlines()))
    for i in range(CHARACTER):
        datas.append(data[i * FEATURE:(i + 1) * FEATURE])
    return datas


def multi_load(paths):
    data = []
    for path in paths:
        lines = load_data(path)
        data.append(lines)
    return data


def write_mean(means):
    paths = list(map(lambda x: '../mean/' + x, ['mean' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    for (i, mean) in enumerate(means):
        with open(paths[i], 'w') as f:
            for line in mean:
                f.write(str(line) + '\n')


def write_covariance(covs):
    paths = list(map(lambda x: '../sigma/' + x, ['sigma' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    for (i, cov) in enumerate(covs):
        with open(paths[i], 'w') as f:
            for line in cov:
                for num in line:
                    f.write('{:.3f},'.format(num))
                f.write('\n')


def load_covariance(paths):
    ans = []
    for (i, path) in enumerate(paths):
        with open(path, 'r') as f:
            buf = []
            for f_line in f:
                buf.append(list(map(lambda x: float(x), f_line.strip().split(',')[:-1])))
            ans.append(buf)
    return ans


def multi_list(l1, l2):
    return [x * y for (x, y) in zip(l1, l2)]


def write_eigenvalues(eigs, paths):
    for i, eig in enumerate(eigs):
        with open(paths[i], 'w') as f:
            for e in eig:
                f.write(str(e)+',')


def write_eigenvalue(eig, path):
    with open(path, 'w') as f:
        for e in eig:
            f.write(str(e)+',')


def load_eigenvalues(paths):
    ans = []
    for path in paths:
        with open(paths, 'r') as f:
            buf = []
            for line in f:
                buf.append(list(map(lambda x: float(x), line.strip().split(',')[:-1])))
            ans.append(buf)
    return ans


def write_vectors(vecs, paths):
    for i, vec in enumerate(vecs):
        with open(paths[i], 'w') as f:
            for line in vec:
                for v in line:
                    f.write(str(v)+',')
                f.write('\n')


def write_vector(vec, path):
    with open(path, 'w') as f:
        for line in vec:
            for v in line:
                f.write(str(v)+',')
            f.write('\n')


def load_vectors(paths):
    ans = []
    for path in paths:
        with open(path, 'r') as f:
            buf = []
            for line in f:
                buf.append(list(map(lambda x: float(x), line.strip().split(',')[:-1])))
            ans.append(buf)
    return ans


class CharDatas(object):
    def __init__(self, datas, covs, x=[]):
        self.datas = datas
        self.means = np.array(self.get_mean())
        self.covs = np.array(covs)
        self.jacobi = self.covs
        self.eigs = np.array([])
        self.vecs = np.array([])
        self.e = np.eye(FEATURE)
        self.x = np.array(x)
        self.label = []

    def get_mean(self):
        bufs = []
        for data in self.datas:
            buf = []
            rev_data = zip(*data)
            for feature in rev_data:
                buf.append(sum(feature))
            buf = list(map(lambda x: x / CHARACTER, buf))
            bufs.append(buf)
        return bufs

    def get_covariance(self):
        answers = []
        for num, data in enumerate(self.datas):
            rev_data = list(zip(*data))
            answer = []
            mean = self.means[num]
            for i in range(FEATURE):
                buf = [((sum(multi_list(rev_data[i], rev_data[j])) / CHARACTER) - mean[i] * mean[j]) for j in
                       range(FEATURE)]
                answer.append(buf)
            answers.append(answer)
        return answers

    def get_eig_jacobi(self):
        eigs_paths = list(map(lambda x: '../eig/' + x, ['eig' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
        vecs_paths = list(map(lambda x: '../vec/' + x, ['vec' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
        start = time.time()
        for num in range(FILES-1):
            for i in range(80000):
                row, col = self.__numpy_find_max_cov(num)
                self.__numpy_sub_jacobi(num, row, col)
            self.eigs = self.eigs.tolist()
            self.eigs.append(np.diag(self.jacobi[num]))
            self.eigs = np.array(self.eigs)
            self.__sort_vecs()
            self.__sort_eigs()
            write_eigenvalue(self.eigs[num], eigs_paths[num])
            write_vector(self.vecs[num], vecs_paths[num])
            print('char numbers =', num)
        end = time.time()
        print('jacobi time =', end-start)

    def __numpy_sub_jacobi(self, num, i, j):
        cov = self.jacobi[num]
        under = cov[j][j]-cov[i][i]
        if under == 0:
            under = 0.01
        theta = 0.5 * math.atan(((2*cov[i][j])/under))
        func_sin = np.frompyfunc(lambda x: math.sin(theta)*x, 1, 1)
        func_cos = np.frompyfunc(lambda x: math.cos(theta)*x, 1, 1)
        func_msin = np.frompyfunc(lambda x: -math.sin(theta)*x, 1, 1)
        if self.vecs.__len__() == num:
            self.vecs = self.vecs.tolist()
            v = np.eye(FEATURE)
            v[i][i] = math.cos(theta)
            v[i][j] = math.sin(theta)
            v[j][i] = -math.sin(theta)
            v[j][j] = math.cos(theta)
            self.vecs.append(v)
            self.vecs = np.array(self.vecs)
        else:
            v = self.vecs[num].T
            v_i = v[i]
            v_j = v[j]
            v_ans_i = func_cos(v_i)+func_msin(v_j)
            v_ans_j = func_sin(v_i)+func_cos(v_j)
            v[i] = v_ans_i
            v[j] = v_ans_j
            self.vecs[num] = v.T
        line_i = cov[i]
        line_j = cov[j]
        line_ans_i = func_cos(line_i)+func_msin(line_j)
        line_ans_j = func_sin(line_i)+func_cos(line_j)
        cov[i] = line_ans_i
        cov[j] = line_ans_j
        cov = cov.T
        line_i = cov[i]
        line_j = cov[j]
        line_ans_i = func_cos(line_i)+func_msin(line_j)
        line_ans_j = func_sin(line_i)+func_cos(line_j)
        cov[i] = line_ans_i
        cov[j] = line_ans_j
        self.jacobi[num] = cov.T

    def __numpy_find_max_cov(self, num):
        cov = self.jacobi[num]
        cov = np.triu(cov, k=1).__abs__()
        index = cov.argmax()
        row = math.floor(index / FEATURE)
        column = index - row * FEATURE
        return row, column

    def __sort_vecs(self):
        buf = []
        self.vecs = self.vecs.tolist()
        self.eigs = self.eigs.tolist()
        for v, e in zip(self.vecs, self.eigs):
            v.append(e)
            v = list(zip(*v))
            v.sort(key=lambda x: x[FEATURE], reverse=True)
            v = list(zip(*v))
            v.__delitem__(FEATURE)
            buf.append(v)
        self.vecs = np.array(buf)
        self.eigs = np.array(self.eigs)

    def __sort_eigs(self):
        self.eigs = self.eigs.tolist()
        for eig in self.eigs:
            eig.sort(reverse=True)
        self.eigs = np.array(self.eigs)

    def maharanobis(self):
        '''
        マハラノビス距離を計算する。

        Returns
        -------

        '''
        for m, e, v in zip(self.means, self.eigs, self.vecs):
            x = self.x
            for line in x:
                line -= m





if __name__ == '__main__':
    datas_paths = list(map(lambda x: '../char/' + x, ['c' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    covs_paths = list(map(lambda x: '../sigma/' + x, ['sigma' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    eigs_paths = list(map(lambda x: '../eig/' + x, ['eig' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    vecs_paths = list(map(lambda x: '../vec/' + x, ['vec' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    char = CharDatas(multi_load(datas_paths), load_covariance(covs_paths))
    np.set_printoptions(precision=1, threshold=100000, linewidth=10000)
