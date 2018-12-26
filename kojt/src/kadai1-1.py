import math
import random

F_SAMP = 40000


def create_sin(n, a, f, p):
    ans = []
    for i in range(n):
        ans.append([i/F_SAMP, a*math.sin((2*math.pi*f*i)/F_SAMP)])
    return ans


def create_bpsk(n, rate):
    ans = []
    for i in range(n):
        buf = random.randrange(2)
        for r in range(rate):
            ans.append([(i*rate+r)/F_SAMP, buf])
    return ans


def create_ask(cs, bs):
    ans = []
    for c, b in zip(cs, bs):
        ans.append([c[0], c[1]*b[1]])
    return ans


if __name__=='__main__':
    c = create_sin(1200, 1, 40000, -math.pi/2)
    b = create_bpsk(6, 200)
    datas = create_ask(c, b)
    with open('../txt/kadai1-3.txt', 'w') as f:
        for data in datas:
            f.write(str(data[0]) + '  ' + str(data[1])+'\n')
