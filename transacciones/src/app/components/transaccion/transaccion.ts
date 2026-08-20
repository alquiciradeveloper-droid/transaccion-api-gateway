import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransaccionService } from '../../services/transaccion.service';
import { AuthService } from '../../services/auth.service';
import { TransaccionResponse, PageableResponse } from '../../models/transaccion.model';

@Component({
  selector: 'app-transaccion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaccion.html',
  styleUrls: ['./transaccion.css']
})
export class TransaccionComponent implements OnInit {
  operacion = 'VENTA';
  importe = '';
  cliente = '';
  secretoPlano = '';

  notificacion: { tipo: 'exito' | 'error'; mensaje: string } | null = null;
  transacciones: TransaccionResponse[] = [];

  paginaActual = 0;
  totalPaginas = 0;
  criterioOrden = 'id';
  direccionOrden = 'desc';

  constructor(
    private transaccionService: TransaccionService, 
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarTransacciones();
  }

  cargarTransacciones(page: number = 0): void {
    const sort = `${this.criterioOrden},${this.direccionOrden}`;
    this.transaccionService.obtenerTransacciones(page, 10, sort).subscribe({
      next: (res) => {
        if ('content' in res && res.content) {
          const pageRes = res as PageableResponse<TransaccionResponse>;
          this.transacciones = pageRes.content || [];
          this.totalPaginas = pageRes.page?.totalPages ?? pageRes.totalPages ?? 1;
          this.paginaActual = pageRes.page?.number ?? pageRes.number ?? page;
        } else {
          this.transacciones = res as TransaccionResponse[];
          this.totalPaginas = 1;
          this.paginaActual = 0;
        }
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error al cargar la lista:', err)
    });
  }

  registrarTransaccion(): void {
    if (!this.secretoPlano || !this.cliente || !this.importe) {
      this.mostrarNotificacion('error', 'Por favor complete todos los campos obligatorios.');
      return;
    }

    this.transaccionService
      .registrarTransaccion(this.operacion, this.importe, this.cliente, this.secretoPlano)
      .subscribe({
        next: (res) => {
          this.mostrarNotificacion(
            'exito', 
            `Transacción registrada. ID: ${res.id} | Referencia: ${res.referencia} | Estatus: ${res.estatus}`
          );
          this.secretoPlano = '';
          this.importe = '';
          this.cliente = '';
          this.cargarTransacciones(this.paginaActual);
        },
        error: (err) => {
          const msg =err.error?.importe || err.error?.cliente || 'Error al registrar la transacción';
          this.mostrarNotificacion('error', `Error al registrar: ${msg}`);
        }
      });
  }

  cancelarTransaccion(transaccion: TransaccionResponse): void {
    if (!confirm(`¿Estás seguro de cancelar la transacción ID ${transaccion.id}?`)) return;

    this.transaccionService.cancelarTransaccion(transaccion.id, transaccion.referencia).subscribe({
      next: (res) => {
        this.mostrarNotificacion('exito', `Transacción ${res.id || transaccion.id} cancelada correctamente.`);
        this.cargarTransacciones(this.paginaActual);
      },
      error: (err) => {
        const msg = err.error?.error || 'No se pudo procesar la cancelación';
        this.mostrarNotificacion('error', `Error al cancelar: ${msg}`);
      }
    });
  }

  cambiarOrden(campo: string): void {
    this.direccionOrden = this.criterioOrden === campo && this.direccionOrden === 'asc' ? 'desc' : 'asc';
    this.criterioOrden = campo;
    this.cargarTransacciones(0);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && (this.totalPaginas === 0 || nuevaPagina < this.totalPaginas)) {
      this.cargarTransacciones(nuevaPagina);
    }
  }

  private mostrarNotificacion(tipo: 'exito' | 'error', mensaje: string): void {
    this.notificacion = { tipo, mensaje };
    this.cdr.detectChanges();
  }

  cerrarSesion(): void {
    this.authService.logout();
  }

  validarDecimales(event: Event): void {
    const input = event.target as HTMLInputElement;
    const valor = input.value;

    if (valor && !/^\d+(\.\d{0,2})?$/.test(valor)) {
      const match = valor.match(/^\d+(\.\d{0,2})?/);
      this.importe = match ? match[0] : '';
      input.value = this.importe;
    }
  }
}