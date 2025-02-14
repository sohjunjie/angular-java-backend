import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { RemoteLoadComponent } from './component/remote-load/remote-load.component';
import { RemotePageComponent } from './component/remote-page/remote-page.component';
import { RemoteIframeComponent } from './component/remote-iframe/remote-iframe.component';

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
            { path: 'remote-load', component: RemoteLoadComponent },
            { path: 'remote-page', component: RemotePageComponent },
            { path: 'remote-iframe', component: RemoteIframeComponent }
        ]
    },
    {
        path: '**',
        redirectTo: 'app',
    },
];
