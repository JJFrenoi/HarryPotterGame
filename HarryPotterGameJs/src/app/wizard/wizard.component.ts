import { Component, Input } from '@angular/core';

import { Wizard } from './wizard';

@Component({
  selector: 'app-wizard',
  templateUrl: './wizard.component.html',
  styleUrls: ['./wizard.component.css'],
})
export class WizardComponent{
  @Input() wizard: Wizard;
  constructor() {}
}
