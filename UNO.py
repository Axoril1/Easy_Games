import random

c = ['R', 'G', 'B', 'Y']
v = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'S', 'R', '+2']
w = ['W', 'W+4']
d = []

for i in c:
    for j in v:
        d.append(i + j)
        if j != '0':
            d.append(i + j)
for i in w:
    d += [i] * 4

random.shuffle(d)

def draw(n):
    return [d.pop() for _ in range(n)]

p = [draw(7), draw(7)]
t = [d.pop()]
s = 0
dr = 0
rev = 1

def can_play(card, top):
    return card[0] == top[0] or card[1:] == top[1:] or card[0] == 'W'

while True:
    h = p[s]
    print('\nTop:', t[-1])
    if s == 0:
        print('Your hand:', ', '.join(h))
        m = [i for i, x in enumerate(h) if can_play(x, t[-1])]
        if not m:
            print('No play, drawing...')
            h += draw(1)
        else:
            print('Playable:', ', '.join(f'{i}:{h[i]}' for i in m))
            x = input('Play card (index): ')
            if x.isdigit() and int(x) in m:
                x = int(x)
                c_ = h.pop(x)
                t.append(c_)
                if c_[0] == 'W':
                    nc = input('Pick color (R/G/B/Y): ').upper()
                    t[-1] = nc + c_[1:]
                if c_[1:] == 'S':
                    s = (s + rev) % 2
                if c_[1:] == 'R':
                    rev *= -1
                if c_[1:] == '+2':
                    dr += 2
                if c_[1:] == '+4':
                    dr += 4
                if not h:
                    print('You win!')
                    break
            else:
                print('Invalid, drawing...')
                h += draw(1)
    else:
        m = [i for i, x in enumerate(h) if can_play(x, t[-1])]
        if not m:
            h += draw(1)
            print('CPU draws.')
        else:
            x = random.choice(m)
            c_ = h.pop(x)
            t.append(c_)
            print('CPU plays:', c_)
            if c_[0] == 'W':
                nc = random.choice(c)
                t[-1] = nc + c_[1:]
                print('CPU picks color:', nc)
            if c_[1:] == 'S':
                s = (s + rev) % 2
            if c_[1:] == 'R':
                rev *= -1
            if c_[1:] == '+2':
                dr += 2
            if c_[1:] == '+4':
                dr += 4
            if not h:
                print('CPU wins!')
                break
    if dr:
        s = (s + rev) % 2
        p[s] += draw(dr)
        print(f'{["You","CPU"][s]} draws {dr}')
        dr = 0
    s = (s + rev) % 2
