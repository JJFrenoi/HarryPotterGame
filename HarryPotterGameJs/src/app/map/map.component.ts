import { Component, OnInit } from '@angular/core';

import { AppComponent } from '../app.component';
import { HttpClient } from '@angular/common/http';
import { Map } from './map';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
})
export class MapComponent implements OnInit {
  mapObservable: Observable<Map>;
  map: Map;
  constructor(private http: HttpClient) {}
  ngOnInit(): void {
    this.mapObservable = this.http.get<Map>(AppComponent.url + '/game/map');
    this.mapObservable.subscribe((map) => {
      this.map = map;
    });
  }
  range(i: number) {
    return Array.from({ length: i }, (_x, i) => i);
  }
}
