import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UmlpageComponent } from './umlpage.component';

describe('UmlpageComponent', () => {
  let component: UmlpageComponent;
  let fixture: ComponentFixture<UmlpageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UmlpageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UmlpageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
