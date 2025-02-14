import { Component, ViewChild, ViewContainerRef } from '@angular/core';
import { AppService } from '../../app.service';

@Component({
  selector: 'app-remote-page',
  standalone: true,
  imports: [],
  templateUrl: './remote-page.component.html',
  styleUrl: './remote-page.component.css'
})
export class RemotePageComponent {

  @ViewChild('appRemote', { read: ViewContainerRef })
  private viewContainerRef!: ViewContainerRef;

  constructor(
    private appService: AppService
  ) {

    this.appService.loadRemoteEntryComponent$("app-frontend-remote", "./Component")
      .subscribe(mod => {

        const component: any = this.viewContainerRef.createComponent(mod.AppComponent);
        Object.assign(component.instance, { token: 456});

      });

  }

}
