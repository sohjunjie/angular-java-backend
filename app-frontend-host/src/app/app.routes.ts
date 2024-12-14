import { Routes } from '@angular/router';
import { RemoteLoadComponent } from './remote-load/remote-load.component';
import { RemotePageComponent } from './remote-page/remote-page.component';

export const routes: Routes = [
    { path: 'remote-load', component: RemoteLoadComponent},
    { path: 'remote-page', component: RemotePageComponent}
];
