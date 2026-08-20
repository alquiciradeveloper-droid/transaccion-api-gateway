import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { TransaccionComponent } from './components/transaccion/transaccion';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'transacciones', component: TransaccionComponent },
  { path: '**', redirectTo: 'login' } 
];
