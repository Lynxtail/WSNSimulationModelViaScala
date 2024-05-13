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

plt.figure(1)

plt.scatter(lambdas, tau)
plt.plot(lambdas, mean_tau, '-o', c='g')
plt.title(r'Зависимость $\tau$ от $\lambda_0$')
plt.xlabel(r'$\lambda_0$, пакетов в секунду')
plt.ylabel(r'$\tau$, секунд')
plt.savefig('fig_tau')

plt.figure(2)
plt.scatter(lambdas, n)
plt.plot(lambdas, mean_n, '-o', c='g')
plt.title(r'Зависимость $\sum_i n_i, i = 1, ..., L$ от $\lambda_0$')
plt.xlabel(r'$\lambda_0$, пакетов в секунду')
plt.ylabel(r'$\sum_i n_i$, пакетов')
plt.savefig('fig_n')

