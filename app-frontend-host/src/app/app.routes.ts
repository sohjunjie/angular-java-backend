import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { loadRemoteModule } from '@angular-architects/native-federation';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'app',
        pathMatch: 'full'
    },
    {
        path: 'app',
        component: AppComponent,
        children: [
            { path: '', redirectTo: 'login', pathMatch: 'full' },
            { path: 'login', component: LoginComponent },
            { path: 'remote-page', loadComponent: () => loadRemoteModule('app-frontend-remote', './Component')
                .then((m) => m.AppComponent)
            }
        ]
    }
];
