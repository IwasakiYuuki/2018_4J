# kadai1-4 s15023 岩崎悠紀
import math
import numpy as np
import time
import pandas as pd

FEATURE = 196
CHARACTER = 180
FILES = 47
ALL_CHARACTER = 200


def write_csv_files(datas, paths):
    for path, data in zip(paths, datas):
        buf = pd.DataFrame(data)
        buf.to_csv(path, index=None, columns=None)


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


def multi_load_x(paths):
    data = []
    for path in paths:
        buf = []
        with open(path) as f:
            buf = list(map(lambda x: float(x), f.readlines()))
        for i in range(CHARACTER, ALL_CHARACTER):
            data.append(buf[i * FEATURE:(i + 1) * FEATURE])
    return data


def write_mean(means):
    paths = list(map(lambda x: '../mean/' + x, ['mean' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    for (i, mean) in enumerate(means):
        with open(paths[i], 'w') as f:
            for line in mean:
                f.write(str(line) + '\n')


def load_mean(paths):
    means = []
    for path in paths:
        mean = []
        with open(path, 'r') as f:
            for line in f:
                mean.append(float(line))
        means.append(mean)
    return means


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
        with open(path, 'r') as f:
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


def fill_b(x):
    if abs(x) > 2500:
        return float(1/x)
    else:
        return float(1/2500)


def get_rate(l):
    l = l.reshape(46, 20).tolist()
    ans = []
    for i, line in enumerate(l):
        ans.append(float(line.count(i)/20)*100)
    ans = np.array(ans).ravel()
    print('average recognition rate =', ans.mean(), '%')
    return ans


def get_mean(datas):
    '''
    与えられたデータから平均を求める。

    Parameters
    ----------
    datas: list
        元データ

    Returns
    -------
    means: list
        datasの平均値のリスト

    '''
    means = []
    for data in datas:
        buf = []
        rev_data = zip(*data)
        for feature in rev_data:
            buf.append(sum(feature))
        buf = list(map(lambda x: x / CHARACTER, buf))
        means.append(buf)
    return means


def get_covariance(datas, means):
    '''
    与えられたデータと，その平均を用いて共分散行列を返す。
    共分散行列は，元データの文字数分あり，文字数一つにつき，元データの特徴量分の実対称行列となる。

    Parameters
    ----------
    datas: list
        元のデータのリスト
    means: list
        datasの平均のリスト

    Returns
    -------
    covs: list
        共分散行列のリスト

    '''
    covs = []
    for num, data in enumerate(datas):
        rev_data = list(zip(*data))
        cov = []
        mean = means[num]
        for i in range(FEATURE):
            buf = [((sum(multi_list(rev_data[i], rev_data[j])) / CHARACTER) - mean[i] * mean[j]) for j in
                   range(FEATURE)]
            cov.append(buf)
        covs.append(cov)
    return covs


def get_eig_by_jacobi(covs):
    """
    ヤコビ法を用いて，共分散行列covsの固有値と固有ベクトルを求め，ファイル出力する。
    二回目のfor文でヤコビ法実行の回数を指定する。

    Parameters
    ----------
    covs: list
        共分散行列

    """
    eigs_paths = list(map(lambda x: '../eig/' + x, ['eig' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    vecs_paths = list(map(lambda x: '../vec/' + x, ['vec' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    eigs = np.array([])
    vecs = np.array([])
    covs = np.array(covs)
    start = time.time()
    for num in range(FILES-1):
        for i in range(80000):
            row, col = find_max_cov(covs, num)
            covs, vecs = sub_jacobi(covs, vecs, num, row, col)
        eigs = eigs.tolist()
        eigs.append(np.diag(covs[num]))
        eigs = np.array(eigs)
        vecs = sort_vecs(eigs, vecs)
        eigs = sort_eigs(eigs)
        write_eigenvalue(eigs[num], eigs_paths[num])
        write_vector(vecs[num], vecs_paths[num])
        print('char numbers =', num)
    end = time.time()
    print('jacobi time =', end-start)


def sub_jacobi(covs, vecs, num, i, j):
    """
    Numpyを用いて，共分散行列covsの指定されたインデックスにヤコビ法を適用する。
    P^-1*A*Pの式を一回実行する。

    Parameters
    ----------
    covs: numpy.ndarray
        ヤコビ行列
    vecs: numpy.ndarray
        固有ベクトル
    num: int
        文字番号
    i: int
        行数
    j: int
        列数

    Returns
    -------
    covs: numpy.ndarray
        ヤコビ法を適用した共分散行列
    vecs: numpy.ndarray
        固有ベクトル

    """
    cov = covs[num]
    under = cov[j][j]-cov[i][i]
    if under == 0:
        under = 0.01
    theta = 0.5 * math.atan(((2*cov[i][j])/under))
    func_sin = np.frompyfunc(lambda x: math.sin(theta)*x, 1, 1)
    func_cos = np.frompyfunc(lambda x: math.cos(theta)*x, 1, 1)
    func_msin = np.frompyfunc(lambda x: -math.sin(theta)*x, 1, 1)
    if vecs.__len__() == num:
        vecs = vecs.tolist()
        v = np.eye(FEATURE)
        v[i][i] = math.cos(theta)
        v[i][j] = math.sin(theta)
        v[j][i] = -math.sin(theta)
        v[j][j] = math.cos(theta)
        vecs.append(v)
        vecs = np.array(vecs)
    else:
        v = vecs[num].T
        v_i = v[i]
        v_j = v[j]
        v_ans_i = func_cos(v_i)+func_msin(v_j)
        v_ans_j = func_sin(v_i)+func_cos(v_j)
        v[i] = v_ans_i
        v[j] = v_ans_j
        vecs[num] = v.T
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
    cov = cov.T
    return covs, vecs


def find_max_cov(covs, num):
    """
    与えられた実対称行列covsの非対角成分の最大値のインデックスを求める。

    Parameters
    ----------
    covs: numpy.ndarray
        対象の実対称行列
    num: int
        実対称行列の番号

    Returns
    -------
    row: int
        最大値の行数
    column: int
        最大値の列数

    """
    cov = covs[num]
    cov = np.triu(cov, k=1).__abs__()
    index = cov.argmax()
    row = math.floor(index / FEATURE)
    column = index - row * FEATURE
    return row, column


def sort_vecs(eigs, vecs):
    """
    行列vecsをベクトルeigsを元に降順にソートする。

    Parameters
    ----------
    eigs: numpy.ndarray
        固有値行列
    vecs: numpy.ndarray
        固有ベクトルの行列

    Returns
    -------
    vecs: numpy.ndarray
        降順のソートされた固有ベクトルの行列

    """
    buf = []
    vecs = vecs.tolist()
    eigs = eigs.tolist()
    for v, e in zip(vecs, eigs):
        v.append(e)
        v = list(zip(*v))
        v.sort(key=lambda x: x[FEATURE], reverse=True)
        v = list(zip(*v))
        v.__delitem__(FEATURE)
        buf.append(v)
    vecs = np.array(buf)
    return vecs


def sort_eigs(eigs):
    """
    与えられた固有値の行列eigsを降順にソートする。

    Parameters
    ----------
    eigs: numpy.ndarray
        固有値の行列

    Returns
    -------
    eigs: numpy.ndarray
        降順にソートされた固有値の行列

    """
    eigs = eigs.tolist()
    for eig in eigs:
        eig.sort(reverse=True)
    eigs = np.array(eigs)
    return eigs


class LearningDatas(object):
    """
    学習データの各情報を保持する。

    Attributes
    ----------
    datas: list
        学習用特徴量データのリスト
    means: numpy.ndarray
        datasの平均値のリスト
    covs: numpy.adarray
        datasの共分散行列のリスト
    eigs: numpy.adarray
        covsの固有値行列のリスト
    vecs: numpy.adarray
        covsの固有ベクトル行列のリスト

    """
    def __init__(self, datas=[], means=[], covs=[], eigs=[], vecs=[]):
        self.datas = datas
        self.means = np.array(means)
        self.covs = np.array(covs)
        self.eigs = np.array(eigs)
        self.vecs = np.array(vecs)


class Recognition(LearningDatas):
    """
    LearningDatasクラスの学習データを元に未知のデータを識別する。

    Attributes
    ----------
    x: tuple
        認識用の未知の特徴量データ

    """
    def __init__(self, datas=[], means=[], covs=[], x=[], eigs=[], vecs=[]):
        super().__init__(datas=datas, means=means, covs=covs, eigs=eigs, vecs=vecs)
        self.x = tuple(x)

    def get_recognition_by_maharanobis(self):
        """
        マハラノビス距離を使い，文字認識をする。

        Returns
        -------
        ans: numpy.ndarray
            認識した文字ラベルのnumpy.ndarray型のリスト

        """
        ans = []
        dis = self.get_maharanobis()
        ans = np.array(dis).argmin(axis=0)
        get_rate(ans.reshape(46, 20))
        print(ans.reshape(46, 20).__str__())
        return ans

    def get_maharanobis(self):
        """
        インスタンス変数xを未知のデータとし，インスタンス変数eigs, vecs, meansの
        固有値，固有ベクトル，平均値を使用してマハラノビス距離を計算する。

        Returns
        -------
        dis: numpy.ndarray
            求めたマハラノビス距離のnumpy.ndarray型のリスト

        """
        pow_func = np.vectorize(lambda x: x**2)
        fill_func = np.vectorize(fill_b)
        dis = []
        for m, e, v in zip(self.means, self.eigs, self.vecs):
            data = np.array(list(self.x))
            for line in data:
                line -= m
            v = np.delete(v.T, slice(180, 196), 1)
            data = np.dot(data, v)
            data = pow_func(data)
            e = fill_func(e)
            data = np.dot(data, e.T[:180])
            data = data.ravel().tolist()
            dis.append(data)
        return dis


if __name__ == '__main__':
    np.set_printoptions(precision=1, threshold=100000, linewidth=10000)
    datas_paths = list(map(lambda x: '../char/' + x, ['c' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    covs_paths = list(map(lambda x: '../sigma/' + x, ['sigma' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    eigs_paths = list(map(lambda x: '../eig/' + x, ['eig' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    vecs_paths = list(map(lambda x: '../vec/' + x, ['vec' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    means_paths = list(map(lambda x: '../mean/' + x, ['mean' + '{:02}'.format(i) + '.txt' for i in range(1, FILES)]))
    eigs = np.array(load_eigenvalues(eigs_paths))
    vecs = np.array(load_vectors(vecs_paths))
    means = np.array(load_mean(means_paths))
    write_csv_files(vecs[0], ['../csv/sigma/eig02.csv'])
    x = np.array(multi_load_x(datas_paths))
    char = Recognition(eigs=eigs, vecs=vecs, means=means, x=x)
    char.get_recognition_by_maharanobis()
