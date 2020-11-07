import { Component, OnInit } from '@angular/core';

import { AppComponent } from '../app.component';
import { Game } from '../gamepage/game';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-historypage',
  templateUrl: './historypage.component.html',
  styleUrls: ['./historypage.component.css'],
})
export class HistorypageComponent implements OnInit {
  gameHistory: Array<Game>;
  title = 'Classement';
  type = 'BarChart';
  data = [];
  options = {
    forceIFrame: true,
    series: {
      0: { color: 'red' },
      1: { color: 'blue' },
    },
    vAxis: {
      minValue: 0,
    },
    colors: ['green', 'blue'],
  };
  width = 550;
  height = 400;
  constructor(private http: HttpClient) {}
  ngOnInit(): void {
    this.http
      .get<Array<Game>>(AppComponent.url + '/history')
      .subscribe((games) => {
        this.gameHistory = games;
        this.dataPromise(this.gameHistory)
          .then((arrayofData) => {
            this.data = arrayofData;
          })
          .catch((error) => console.warn(error))
          .finally(() => console.log(this.data));
        console.log(this.data);
      });
  }
  dataPromise(games: Array<Game>) {
    return new Promise<Array<Array<string | number>>>((resolve, rejects) => {
      if (games.length === 0) {
        rejects('there is no game in the database');
      } else {
        const winofGryffindor = ['Gryffindor', 0];
        const winofHufflepuff = ['Hufflepuff', 0];
        const winofRavenclaw = ['Ravenclaw', 0];
        const winofSlytherin = ['Slytherin', 0];
        winofGryffindor[1] = games.filter(
          (game) => game.winner === 'Gryffindor'.toUpperCase()
        ).length;
        winofHufflepuff[1] = games.filter(
          (game) => game.winner === 'Hufflepuff'.toUpperCase()
        ).length;
        winofRavenclaw[1] = games.filter(
          (game) => game.winner === 'Ravenclaw'.toUpperCase()
        ).length;
        winofSlytherin[1] = games.filter(
          (game) => game.winner === 'Slytherin'.toUpperCase()
        ).length;
        resolve([
          winofGryffindor,
          winofHufflepuff,
          winofRavenclaw,
          winofSlytherin,
        ]);
      }
    });
  }
}
