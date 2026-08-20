import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, from, switchMap } from 'rxjs';
import { CryptoUtil } from '../crypto.util';
import { 
  TransaccionRequest, 
  TransaccionResponse, 
  CancelarTransaccionRequest, 
  PageableResponse 
} from '../models/transaccion.model';

@Injectable({
  providedIn: 'root'
})
export class TransaccionService {
  private readonly apiUrl = 'http://localhost:8080/api/transacciones';

  constructor(private http: HttpClient) {}

  obtenerTransacciones(
    page: number = 0, 
    size: number = 10, 
    sort: string = 'id,desc'
  ): Observable<PageableResponse<TransaccionResponse> | TransaccionResponse[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get<PageableResponse<TransaccionResponse> | TransaccionResponse[]>(
      this.apiUrl, 
      { params }
    );
  }

  registrarTransaccion(
    operacion: string, 
    importe: string, 
    cliente: string, 
    secretoPlano: string
  ): Observable<TransaccionResponse> {
    return from(CryptoUtil.encrypt(secretoPlano)).pipe(
      switchMap((secretoCifrado) => {
        const body: TransaccionRequest = {
          operacion: operacion.toUpperCase(),
          importe: parseFloat(importe).toFixed(2),
          cliente,
          secreto: secretoCifrado
        };
        return this.http.post<TransaccionResponse>(this.apiUrl, body);
      })
    );
  }

  cancelarTransaccion(id: number, referencia: string): Observable<TransaccionResponse> {
    const body: CancelarTransaccionRequest = {
      id,
      referencia,
      estatus: 'cancelar'
    };
    return this.http.patch<TransaccionResponse>(this.apiUrl, body);
  }
}