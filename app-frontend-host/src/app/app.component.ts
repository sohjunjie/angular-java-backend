import { loadRemoteModule } from '@angular-architects/native-federation';
import { Component, ViewChild, ViewContainerRef } from '@angular/core';
import { AppService } from './app.service';
import { RemoteEntryConfig } from './model/remote-entry-config';


@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {


  @ViewChild('appMfe', { read: ViewContainerRef})
  private viewContainerRef!: ViewContainerRef;

  constructor(
    private appService: AppService
  ) {

  }

  ngOnInit() {
    this.load();
  }


  async load(): Promise<void> {

    const data: RemoteEntryConfig[] = await this.appService.getFederationJson();

    this.loadRemoteContainer(data, "app-mfe", this.viewContainerRef);

  }

  async loadRemoteContainer(data: RemoteEntryConfig[], name: string, container: ViewContainerRef) {

    console.log(data);

    const config: RemoteEntryConfig | undefined = data.find(obj => obj["name"] === name);
    if(config && config.exposedModule) {
      const module = await loadRemoteModule({
        remoteEntry: config.remoteEntry,
        exposedModule: config.exposedModule
      });
      container.createComponent(module.AppComponent);
    }
  }

}
