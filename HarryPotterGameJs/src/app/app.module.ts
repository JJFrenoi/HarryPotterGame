import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { BoxComponent } from './box/box.component';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { ConfigpageComponent } from './configpage/configpage.component';
import { DementorComponent } from './dementor/dementor.component';
import { GamepageComponent } from './gamepage/gamepage.component';
import { GoogleChartsModule } from 'angular-google-charts';
import { HistorypageComponent } from './historypage/historypage.component';
import { HttpClientModule } from '@angular/common/http';
import { MapComponent } from './map/map.component';
import { MovableService } from './wizard/movable.service';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule } from '@angular/forms';
import { UmlpageComponent } from './umlpage/umlpage.component';
import { WizardComponent } from './wizard/wizard.component';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    BoxComponent,
    WizardComponent,
    GamepageComponent,
    ConfigpageComponent,
    HistorypageComponent,
    UmlpageComponent,
    DementorComponent,
  ],
  imports: [
    BrowserModule,
    GoogleChartsModule,
    CommonModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [MovableService],
  bootstrap: [AppComponent],
})
export class AppModule {}
