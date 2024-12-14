import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemotePageComponent } from './remote-page.component';

describe('RemotePageComponent', () => {
  let component: RemotePageComponent;
  let fixture: ComponentFixture<RemotePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemotePageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RemotePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
