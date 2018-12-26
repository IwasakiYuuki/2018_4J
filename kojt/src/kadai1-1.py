import math

F_SAMP = 40000


def create_sin(n, a, f, p):
    ans = []
    for i in range(n):
        ans.append([i/F_SAMP, a*math.sin((2*math.pi*f*i)/F_SAMP)])
    return ans


if __name__=='__main__':
    datas = create_sin(1000, 1, 200, 0)
    with open('../txt/kadai1-1.txt', 'w') as f:
        for data in datas:
            f.write(str(data[0]) + '  ' + str(data[1])+'\n')
