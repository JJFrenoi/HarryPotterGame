import { Component, Input, OnDestroy, OnInit } from '@angular/core';

import { Box } from './box';
import { Dementor } from './../dementor/dementor';
import { MovableService } from '../wizard/movable.service';
import { Subscription } from 'rxjs';
import { Wizard } from './../wizard/wizard';

@Component({
  selector: 'app-box',
  templateUrl: './box.component.html',
  styleUrls: ['./box.component.css'],
})
export class BoxComponent implements OnInit, Box, OnDestroy {
  @Input() id: number;
  @Input() posX: number;
  @Input() posY: number;
  @Input() color: string;
  @Input() isObstacle: boolean;
  display: string | null = null;
  wizardSubcription: Subscription;
  dementorSubcription : Subscription;
  constructor(private wizardservice: MovableService) {}

  ngOnDestroy(): void {
    if (this.wizardSubcription !== undefined) {
      this.wizardSubcription.unsubscribe();
      this.dementorSubcription.unsubscribe();
    }
  }
  ngOnInit(): void {
    this.display = this.isObstacle ? 'O' : null;
    if (this.display === null) {
      this.wizardSubcription = this.wizardservice.currentWizard.subscribe(
        (wizards) => {
          if(wizards.length > 0 ){
            this.displayPomiseWizards(wizards).then((display)=>(this.display = display));
          }
        }
      );
      this.dementorSubcription = this.wizardservice.currentDementors.subscribe((dementors)=>{
        if(dementors.length > 0 ){
          this.displayDementorsPromise(dementors).then((display)=>(this.display = display));
        }
      });
    }
  }
  displayPomiseWizards(wizards: Wizard[]) {
    return new Promise<string | null>((resolve) => {
      const w = wizards.filter((wizard) => wizard.currentBox.id === this.id);
      if (w.length === 1) {
        w[0].master ? resolve('M') : resolve(w[0].house.icon);
        }else {
          resolve(null);
        }
    });
  }
  displayDementorsPromise(dementors: Dementor[]) {
    return new Promise<string>((resolve) => {
      const d = dementors.filter(
        (dementor) => dementor.currentBox.id === this.id
      );
      if (d.length === 1) {
        resolve(d[0].icon);
      }
    });
  }
}
