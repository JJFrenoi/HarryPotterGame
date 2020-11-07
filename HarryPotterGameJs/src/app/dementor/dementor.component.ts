import { Component, Input, OnInit } from '@angular/core';

import { Dementor } from './dementor';

@Component({
  selector: 'app-dementor',
  templateUrl: './dementor.component.html',
  styleUrls: ['./dementor.component.css'],
})
export class DementorComponent {
  @Input() dementor: Dementor;
}
