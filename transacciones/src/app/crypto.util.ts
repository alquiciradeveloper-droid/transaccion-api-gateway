export class CryptoUtil {
  // Clave exacta de 32 bytes/caracteres 
  private static readonly SECRET_KEY_STRING = '12345678901234567890123456789012';

  static async encrypt(plainText: string): Promise<string> {
    const encoder = new TextEncoder();
    
    const keyData = encoder.encode(this.SECRET_KEY_STRING);
    const dataToEncrypt = encoder.encode(plainText);

    const cryptoKey = await window.crypto.subtle.importKey(
      'raw',
      keyData,
      { name: 'AES-GCM' },
      false,
      ['encrypt']
    );

    const iv = window.crypto.getRandomValues(new Uint8Array(12));

    const encryptedBuffer = await window.crypto.subtle.encrypt(
      {
        name: 'AES-GCM',
        iv: iv,
        tagLength: 128 
      },
      cryptoKey,
      dataToEncrypt
    );

    
    const combined = new Uint8Array(iv.length + encryptedBuffer.byteLength);
    combined.set(iv, 0);
    combined.set(new Uint8Array(encryptedBuffer), iv.length);

   
    let binary = '';
    combined.forEach((byte) => (binary += String.fromCharCode(byte)));
    return btoa(binary);
  }
}