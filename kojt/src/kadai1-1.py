import math
import random

F_SAMP = 40000
bpsk = [0, 0, 1, 0, 1, 1, 0]


def create_sin(n, a, f, p):
    ans = []
    for i in range(n):
        ans.append([i/F_SAMP, a*math.sin((2*math.pi*f*i)/F_SAMP)])
    return ans


def create_cos(n, a, f, p):
    ans = []
    for i in range(n):
        ans.append([i/F_SAMP, a*math.cos((2*math.pi*f*i)/F_SAMP)])
    return ans


def create_bpsk(n, rate):
    ans = []
    for i in range(n):
        buf = bpsk[i]
        for r in range(rate):
            ans.append([(i*rate+r)/F_SAMP, buf])
    return ans


def create_ask(cs, bs):
    ans = []
    print(cs.__len__())
    print(bs.__len__())
    for c, b in zip(cs, bs):
        ans.append([c[0], c[1]*b[1]])
    return ans


def LPF(r, LPFactor):
    ans = [[0, 0]]
    for i in range(1, r.__len__()):
        ans.append([r[i][0], r[i][1] + (ans[i-1][1] - r[i][1])*LPFactor])
    return ans


if __name__ == '__main__':
    c = create_cos(1200, 1, 2000, -math.pi/2)
    b = create_bpsk(6, 200)
    vask = create_ask(c, b)
    r = create_ask(vask, create_cos(1200, 1, 2000, -math.pi/2))
    r = LPF(r, 0.9)
    with open('../txt/kadai1-1.txt', 'w') as f:
        for a in c:
            f.write(str(a[0]) + '  ' + str(a[1])+'\n')
    with open('../txt/kadai1-2.txt', 'w') as f:
        for a in b:
            f.write(str(a[0]) + '  ' + str(a[1])+'\n')
    with open('../txt/kadai1-3.txt', 'w') as f:
        for data in vask:
            f.write(str(data[0]) + '  ' + str(data[1])+'\n')
    with open('../txt/kadai1-4.txt', 'w') as f:
        for data in r:
            f.write(str(data[0]) + '  ' + str(data[1])+'\n')
