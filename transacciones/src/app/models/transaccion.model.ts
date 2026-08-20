export interface TransaccionRequest {
  operacion: string;
  importe: string;
  cliente: string;
  secreto: string;
}

export interface TransaccionResponse {
  id: number;
  referencia: string;
  estatus: string;
  operacion?: string;
  importe?: number;
  cliente?: string;
}

export interface CancelarTransaccionRequest {
  id: number;
  referencia: string;
  estatus: string;
}

export interface PageableResponse<T> {
  content?: T[];
  page?: { totalPages: number; number: number };
  totalPages?: number;
  number?: number;
}