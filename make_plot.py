import numpy as np
import os
import os.path
import matplotlib.pyplot as plt

os.system('sbt ~run')


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
print(f'Total: {counter}/{len(lambdas) * len(values[-1])}\n')


mean_demands = []
mean_tau = []
mean_n_i = []
mean_n = []
mean_Lambda = []
print(lambdas)
plot_multiplier = 30
lambdas = list(np.repeat(lambdas, plot_multiplier))
for i in range(0, len(demands), plot_multiplier):
    mean_demands.append(np.mean(demands[i:i+plot_multiplier], axis=0))
    mean_tau.append(np.mean(tau[i:i+plot_multiplier]))
    mean_n_i.append(np.mean(n_i[i:i+plot_multiplier], axis=0))
    mean_n.append(np.mean(n[i:i+plot_multiplier]))
    mean_Lambda.append(np.mean(Lambda[i:i+plot_multiplier]))
    print(f'lambda_0: {lambdas[i]}')
    print(f'Требования: {mean_demands[-1]}')
    print(f'tau_0: {mean_tau[-1]}')
    print(f'n_i: {mean_n_i[-1]}')
    print(f'n: {mean_n[-1]}')
    print(f'Lambda: {mean_Lambda[-1]}')
    print('_'*30, end='\n\n')

print(f'\nТребования: {np.mean(mean_demands, axis=0)}')
print(f'tau_0: {np.mean(mean_tau)}')
print(f'n_i: {np.mean(mean_n_i, axis=0)}')
print(f'n: {np.mean(mean_n)}')
print(f'Lambda: {np.mean(mean_Lambda)}')

lambda_0 = np.array((96.15385, 192.30769, 288.46154, 384.61538, 480.76923, 576.92308))
lambda_0_for_classic_network = np.array((96.15385, 192.30769, 288.46154, 384.61538, 480.76923, 576.92308))
tau_for_plot = np.array((0.00186, 0.00198, 0.00211, 0.00227, 0.00248, 0.00275))
tau_for_classic_network = np.array((0.00201, 0.00207, 0.00214, 0.00221, 0.00229, 0.00238))
n_for_plot = np.array((0.1792 , 0.37989, 0.60809, 0.87305, 1.19005, 1.58725))
n_for_classic_network = np.array((0.19316, 0.39849, 0.61739, 0.85155, 1.10294, 1.37396))

dx = max(abs(mean_tau - tau_for_plot))
i_dx = abs(mean_tau - tau_for_plot).argmax()
x = mean_tau[i_dx]
print(f'\nМаксимальная погрешность tau_0: {dx / x * 100}%')

dx = max(abs(mean_n - n_for_plot))
i_dx = abs(mean_n - n_for_plot).argmax()
x = mean_n[i_dx]
print(f'Максимальная погрешность sum n_i: {dx / x * 100}%')


mean_tau = np.array(np.repeat(mean_tau, plot_multiplier))
mean_n = np.array(np.repeat(mean_n, plot_multiplier))

plt.rcParams.update({'font.size' : 8})

plt.figure(1)
plt.scatter(lambdas, tau, c='black')
plt.plot(lambdas, mean_tau, '-o', c='black', label='ИМ - ненадёжная сеть')
plt.grid(True)
# plt.plot(lambda_0_for_classic_network, tau_for_classic_network, '-o',  c='b', label='АМ - надёжная сеть')
plt.plot(lambda_0, tau_for_plot, '-o', c='grey', label='АМ - ненадёжная сеть')
plt.ticklabel_format(axis='y', scilimits=[0, 0])
plt.legend()
plt.title(f'Зависимость оценки времени реакции сети\nот интенсивности входящего потока\nпри пороге ' + r"$\tau'_0$" + f'= 1')
plt.xlabel(r'$\lambda_0$, пакетов в секунду')
plt.ylabel(r'$\tau$, секунд')
plt.savefig('IM_AM_fig_tau.png', format='png', dpi=1000.)
plt.savefig('IM_AM_fig_tau.svg', format='svg', dpi=1000.)

plt.figure(2)
plt.scatter(lambdas, n, c='black')
plt.plot(lambdas, mean_n, '-o', c='black', label='ИМ - ненадёжная сеть')
plt.grid(True)
# plt.plot(lambda_0_for_classic_network, n_for_classic_network, '-o',  c='b', label='АМ - надёжная сеть')
plt.plot(lambda_0, n_for_plot, '-o', c='grey', label='АМ - ненадёжная сеть')
plt.legend()
plt.title(f'Зависимость оценки среднего числа требований в сети\nот интенсивности входящего потока\nпри пороге ' + r"$\tau'_0$" + f'= 1')
plt.xlabel(r'$\lambda_0$, пакетов в секунду')
plt.ylabel(r'$\sum_i n_i$, пакетов')
plt.savefig('IM_AM_fig_n.png', format='png', dpi=1000.)
plt.savefig('IM_AM_fig_n.svg', format='svg', dpi=1000.)

plt.figure(3)
# plt.scatter(lambdas, tau, c='g')
# plt.plot(lambdas, mean_tau, '-o', c='g', label='ИМ - ненадёжная сеть')
plt.grid(True)
plt.plot(lambda_0_for_classic_network, tau_for_classic_network, '-o',  c='grey', label='АМ - надёжная сеть')
plt.plot(lambda_0, tau_for_plot, '-o', c='black', label='АМ - ненадёжная сеть')
plt.ticklabel_format(axis='y', scilimits=[0, 0])
plt.legend()
plt.title(f'Зависимость м.о. времени реакции сети\nот интенсивности входящего потока\nпри пороге ' + r"$\tau'_0$" + f'= 1')
plt.xlabel(r'$\lambda_0$, пакетов в секунду')
plt.ylabel(r'$\tau_0$, секунд')
plt.savefig('AM_fig_tau.png', format='png', dpi=1000.)
plt.savefig('AM_fig_tau.svg', format='svg', dpi=1000.)

plt.figure(4)
# plt.scatter(lambdas, n, c='g')
# plt.plot(lambdas, mean_n, '-o', c='g', label='ИМ - ненадёжная сеть')
plt.grid(True)
plt.plot(lambda_0_for_classic_network, n_for_classic_network, '-o',  c='grey', label='АМ - надёжная сеть')
plt.plot(lambda_0, n_for_plot, '-o', c='black', label='АМ - ненадёжная сеть')
plt.legend()
plt.title(f'Зависимость м.о. числа требований в сети\nот интенсивности входящего потока\nпри пороге ' + r"$\tau'_0$" + f'= 1')
plt.xlabel(r'$\lambda_0$, пакетов в секунду')
plt.ylabel(r'$\sum_i n_i$, пакетов')
plt.savefig('AM_fig_n.png', format='png', dpi=1000.)
plt.savefig('AM_fig_n.svg', format='svg', dpi=1000.)
