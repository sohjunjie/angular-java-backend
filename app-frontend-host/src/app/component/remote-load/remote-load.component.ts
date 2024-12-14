import { Component, ViewChild, ViewContainerRef } from '@angular/core';
import { AppService } from '../../app.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-remote-load',
  standalone: true,
  imports: [],
  templateUrl: './remote-load.component.html',
  styleUrl: './remote-load.component.css'
})
export class RemoteLoadComponent {

  @ViewChild('appRemote', { read: ViewContainerRef })
  private viewContainerRef!: ViewContainerRef;

  constructor(
    private appService: AppService,
    private router: Router
  ) {

  }

  loadRemote() {

    this.appService.loadRemoteEntryComponent$("app-frontend-remote", "./Component")
      .subscribe(mod => this.viewContainerRef.createComponent(mod.AppComponent));

  }

  navigateRemoteRoute() {
    this.router.navigate(['app', 'remote-route']);
  }

}
