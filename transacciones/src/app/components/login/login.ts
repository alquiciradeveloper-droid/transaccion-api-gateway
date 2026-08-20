import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  usuario = '';
  password = '';
  errorMensaje = '';
  cargando = false;

  constructor(
    private authService: AuthService, 
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  onLogin(): void {
    if (!this.validarFormulario()) return;

    this.cargando = true;
    this.errorMensaje = '';

    const payload: LoginRequest = {
      usuario: this.usuario.trim(),
      password: this.password
    };

    this.authService.login(payload).subscribe({
      next: (respuesta) => {
        this.cargando = false;
        if (respuesta === false) {
          this.errorMensaje = 'Usuario o contraseña incorrectos.';
          return;
        }
        this.router.navigate(['/transacciones']);
      },
      error: (err) => {
        this.cargando = false;
        this.errorMensaje = err.error?.error || 'Usuario o contraseña incorrectos.';

        this.cdr.detectChanges();
      }
    });
  }

  private validarFormulario(): boolean {
    if (!this.usuario.trim() || !this.password.trim()) {
      this.errorMensaje = 'Por favor ingresa usuario y contraseña.';
      return false;
    }
    return true;
  }
}