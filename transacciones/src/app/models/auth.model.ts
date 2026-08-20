export interface LoginRequest {
  usuario: string;
  password: string;
}

export interface LoginResponse {
  exito?: boolean;
  mensaje?: string;
  token?: string;
}