import { BehaviorSubject } from 'rxjs';
import { Dementor } from './../dementor/dementor';
import { Injectable } from '@angular/core';
import { Wizard } from './wizard';

@Injectable()
export class MovableService {
  private wizards: Wizard[] = [];
  private dementors : Dementor[] = [];
  private dementorSource = new BehaviorSubject(this.dementors);
  private wizardSource = new BehaviorSubject(this.wizards);
  currentWizard = this.wizardSource.asObservable();
  currentDementors = this.dementorSource.asObservable();
  constructor() {}
  setWizard(wizards: Wizard[]) {
    this.wizardSource.next(wizards);
  }
  setDementors(demontors: Dementor[]){
    this.dementorSource.next(demontors);
  }
}
