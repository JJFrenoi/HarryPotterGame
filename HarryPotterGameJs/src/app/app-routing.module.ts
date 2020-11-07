import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { ConfigpageComponent } from './configpage/configpage.component';
import { GamepageComponent } from './gamepage/gamepage.component';
import { HistorypageComponent } from './historypage/historypage.component';
import { NgModule } from '@angular/core';
import { UmlpageComponent } from './umlpage/umlpage.component';

const routes: Routes = [
  { path: 'gamepage', component: GamepageComponent },
  { path: 'history', component: HistorypageComponent },
  { path: 'uml', component: UmlpageComponent },
  { path: 'config', component: ConfigpageComponent },
  { path: '', component: AppComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
