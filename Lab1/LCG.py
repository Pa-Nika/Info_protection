class LCG:
    def __init__(self, seed, a, c, m):
        self.state = seed
        self.a = a
        self.c = c
        self.m = m

    def next(self):
        self.state = (self.a * self.state + self.c) % self.m
        return self.state


def encrypt(text, key1):
    encrypted_str = ''
    lcg = LCG(key1, a=1664525, c=1013904223, m=2**32)
    for char in text:
        encrypted_char = ord(char) ^ lcg.next()
        encrypted_str += chr(encrypted_char % 256)
    return encrypted_str


def decrypt(encrypted_text, key1):
    decrypted_str = ""
    lcg = LCG(key1, a=1664525, c=1013904223, m=2 ** 32)
    for char in encrypted_text:
        decrypted_char = ord(char) ^ lcg.next()  # XOR операция с псевдослучайным числом
        decrypted_str += chr(decrypted_char % 256)
    return decrypted_str


def lcg_work():
    # Ввод строки с клавиатуры
    input_text = input("Введите строку: ")
    key = int(input("Введите ключ: "))

    # Шифрование
    encrypted_text = encrypt(input_text, key)
    print("Зашифрованная строка:", encrypted_text)

    # Расшифрование
    decrypted_text = decrypt(encrypted_text, key)
    print("Расшифрованная строка:", decrypted_text)
