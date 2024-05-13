import numpy as np
import os
import os.path
import matplotlib.pyplot as plt

# for _ in range(10):
#     os.system('sbt ~run')

excluded = ('.bloop', '.bsp', '.metals', '.vscode', 'project', 'src', 'target', '.git')
lambdas = []
demands = []
tau = []
n_i = []
n = []
Lambda = []

for item in os.listdir():
    if os.path.isdir(item) and not item in excluded:
        lambdas.append(float(item))
lambdas = sorted(lambdas)

counter = 0
values = []
for i in range(len(lambdas)):
    values.append([])
    for value in os.listdir(str(lambdas[i])):
        values[i].append(value)
    values[i] = sorted(values[i])
    for value in values[i]:
        try:
            with open(str(lambdas[i]) + '/' + value, 'r') as f:
                demands.append(tuple(map(float, f.readline()[1:-2].split(', '))))
                tau.append(float(f.readline()))
                n_i.append(tuple(map(float, f.readline()[1:-2].split(', '))))
                n.append(float(f.readline()))
                Lambda.append(float(f.readline()))
            print(f'{round(lambdas[i], 3)} — {value} — done')
            counter += 1
        except Exception:
            print(f'{round(lambdas[i], 3)} — {value} — error')
print(f'Total: {counter}/60\n')


mean_demands = []
mean_tau = []
mean_n_i = []
mean_n = []
mean_Lambda = []
print(lambdas)
lambdas = list(np.repeat(lambdas, 10))
for i in range(0, len(demands), 10):
    mean_demands.append(np.mean(demands[i:i+10], axis=0))
    mean_tau.append(np.mean(tau[i:i+10]))
    mean_n_i.append(np.mean(n_i[i:i+10], axis=0))
    mean_n.append(np.mean(n[i:i+10]))
    mean_Lambda.append(np.mean(Lambda[i:i+10]))
    print(f'lambda_0: {lambdas[i]}')
    print(f'Требования: {mean_demands[-1]}')
    print(f'tau: {mean_tau[-1]}')
    print(f'n_i: {mean_n_i[-1]}')
    print(f'n: {mean_n[-1]}')
    print(f'Lambda: {mean_Lambda[-1]}')
    print('_'*30, end='\n\n')

print(f'\nТребования: {np.mean(mean_demands, axis=0)}')
print(f'tau: {np.mean(mean_tau)}')
print(f'n_i: {np.mean(mean_n_i, axis=0)}')
print(f'n: {np.mean(mean_n)}')
print(f'Lambda: {np.mean(mean_Lambda)}')

mean_tau = list(np.repeat(mean_tau, 10))
mean_n = list(np.repeat(mean_n, 10))

lambda_0 = (96.15385, 192.30769, 288.46154, 384.61538, 480.76923, 576.92308)
lambda_0_for_classic_network = (96.15385, 192.30769, 288.46154, 384.61538, 480.76923, 576.92308)
tau_for_plot = (0.0021,  0.00223, 0.00238, 0.00256, 0.00278, 0.00309)
tau_for_classic_network = (0.00228, 0.00236, 0.00245, 0.00254, 0.00264, 0.00275)
n_for_plot = (0.20186, 0.4281,  0.68532, 0.98351, 1.3389,  1.78088)
n_for_classic_network = (0.21947, 0.454,   0.70548, 0.97618, 1.26886, 1.58694)




plt.figure(1)
plt.scatter(lambdas, tau, c='g')
plt.plot(lambdas, mean_tau, '-o', c='g', label='ИМ - ненадёжная сеть')
plt.grid(True)
plt.plot(lambda_0_for_classic_network, tau_for_classic_network, '-o',  c='b', label='АМ - надёжная сеть')
plt.plot(lambda_0, tau_for_plot, '-o', c='r', label='АМ - ненадёжная сеть')
plt.legend()
plt.title(f'Зависимость м.о. времени реакции сети\nот интенсивности входящего потока\nпри пороге ' + r"$\tau'_0$" + f'= 1')
plt.xlabel(r'$\lambda_0$, пакетов в секунду')
plt.ylabel(r'$\tau$, секунд')
plt.savefig('fig_tau.png', format='png', dpi=1000.)
plt.savefig('fig_tau.svg', format='svg', dpi=1000.)

plt.figure(2)
plt.scatter(lambdas, n, c='g')
plt.plot(lambdas, mean_n, '-o', c='g', label='ИМ - ненадёжная сеть')
plt.grid(True)
plt.plot(lambda_0_for_classic_network, n_for_classic_network, '-o',  c='b', label='АМ - надёжная сеть')
plt.plot(lambda_0, n_for_plot, '-o', c='r', label='АМ - ненадёжная сеть')
plt.legend()
plt.title(f'Зависимость м.о. числа требований в сети\nот интенсивности входящего потока\nпри пороге ' + r"$\tau'_0$" + f'= 1')
plt.xlabel(r'$\lambda_0$, пакетов в секунду')
plt.ylabel(r'$\sum_i n_i$, пакетов')
plt.savefig('fig_n.png', format='png', dpi=1000.)
plt.savefig('fig_n.svg', format='svg', dpi=1000.)

