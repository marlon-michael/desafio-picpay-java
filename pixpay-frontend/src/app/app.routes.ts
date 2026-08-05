import { Routes } from '@angular/router';
import { HomePage } from './pages/home-page/home-page';

export const routes: Routes = [
    {
        path: "",
        component: HomePage
    },
    // LAZY LOAD
    {
        path: 'register',
        loadComponent: () => import('./pages/register-page/register-page').then(module => module.RegisterPage)
    },
    {
        path: 'account',
        loadComponent: () => import('./pages/list-accounts-page/list-accounts-page').then(module => module.ListAccountsPage)
    },
];
