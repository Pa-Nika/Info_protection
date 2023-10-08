from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.backends import default_backend
import os


class AES:
    def __init__(self, key_):
        self.key = key_

    def complement_block(self, data):
        block_size = algorithms.AES.block_size // 8
        compl_length = block_size - (len(data) % block_size)
        padding = bytes([compl_length]) * compl_length
        return data + padding

    def delete_part_block(self, data):
        compl_length = data[-1]
        return data[:-compl_length]

    def encrypt(self, plaintext):
        plaintext = self.complement_block(plaintext)
        init_vector = os.urandom(algorithms.AES.block_size // 8)
        cipher = Cipher(algorithms.AES(self.key), modes.CFB(init_vector), backend=default_backend())
        encryptor = cipher.encryptor()
        ciphertext = encryptor.update(plaintext) + encryptor.finalize()
        return init_vector + ciphertext

    def decrypt(self, data):
        iv = data[:algorithms.AES.block_size // 8]
        cipher_text = data[algorithms.AES.block_size // 8:]
        cipher = Cipher(algorithms.AES(self.key), modes.CFB(iv), backend=default_backend())
        decryptor = cipher.decryptor()
        decrypted_text = decryptor.update(cipher_text) + decryptor.finalize()
        decrypted_text = self.delete_part_block(decrypted_text)
        return decrypted_text


    def check_avalanche_effect(self, plaintext):
        key = os.urandom(algorithms.AES.block_size // 8)
        cipher = Cipher(algorithms.AES(key), modes.CFB(os.urandom(algorithms.AES.block_size // 8)),
                        backend=default_backend())
        encryptor = cipher.encryptor()
        original_ciphertext = encryptor.update(plaintext) + encryptor.finalize()

        # Изменяем один бит в исходных данных
        modified_plaintext = bytes([b ^ 1 for b in plaintext])

        encryptor = cipher.encryptor()
        modified_ciphertext = encryptor.update(modified_plaintext) + encryptor.finalize()

        avalanche_effect = sum(bit1 != bit2 for bit1, bit2 in zip(original_ciphertext, modified_ciphertext))
        return avalanche_effect / len(original_ciphertext)

def aes_work():
    key = os.urandom(algorithms.AES.block_size // 8)
    aes_cipher = AES(key)

    with open('file.txt', 'rb') as file:
        plaintext = file.read()
    encrypted_data = aes_cipher.encrypt(plaintext)
    with open('encrypted_file.txt', 'wb') as file:
        file.write(encrypted_data)

    with open('encrypted_file.txt', 'rb') as file:
        encrypted_data = file.read()
    decrypted_text = aes_cipher.decrypt(encrypted_data)
    with open('decrypted_file.txt', 'wb') as file:
        file.write(decrypted_text)

    # лавинный эффект
    plaintext_data = os.urandom(16)
    avalanche_effect_ratio = aes_cipher.check_avalanche_effect(plaintext_data)
    print(f"Avalanche Effect Ratio: {avalanche_effect_ratio}")
