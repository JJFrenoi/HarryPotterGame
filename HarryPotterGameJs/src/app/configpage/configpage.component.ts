import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { interval, throwError } from 'rxjs';

import { AppComponent } from './../app.component';
import { Game } from '../gamepage/game';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    Authorization: 'my-auth-token',
  }),
};
@Component({
  selector: 'app-configpage',
  templateUrl: './configpage.component.html',
  styleUrls: ['./configpage.component.css'],
})
export class ConfigpageComponent implements OnInit {
  error: string = '';
  pressOnce = false;
  url: string;
  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      console.error(
        `Backend returned code ${error.status}, ` + `body was: ${error.error}`
      );
    }
    return throwError('Something bad happened; please try again later.');
  }
  speeds = [
    { id: '1', speed: 1000 },
    { id: '2', speed: 500 },
    { id: '3', speed: 250 },
  ];
  gameForm = new FormGroup({
    nbLine: new FormControl('15', Validators.required),
    nbColumn: new FormControl('25', Validators.required),
    nbCharacteres: new FormControl('4', Validators.required),
    obstacle: new FormControl('4', Validators.required),
    nbOfRound: new FormControl('500', Validators.required),
    liveGame: new FormControl(true, Validators.required),
    speedForm: new FormControl(this.speeds[1], Validators.required),
  });
  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {}

  async onSubmit() {
    this.pressOnce = true;
    try {
      const initArray = await this.getDataForm();
      const initGame = await this.initGame(initArray);
      initGame.liveGame
        ? this.router.navigate([
            'gamepage',
            { speed: initGame.speed, nbOfRound: initGame.nbOfRound },
          ])
        : this.waitForWinner();
    } catch (error) {
      this.error = error;
      this.pressOnce = false;
      console.error(error);
    }
  }
  getDataForm() {
    return new Promise<Game>((resolve, rejects) => {
      const nbLine = this.gameForm.get('nbLine').value;
      const nbColumn = this.gameForm.get('nbColumn').value;
      const nbCharacteres = this.gameForm.get('nbCharacteres').value;
      const obstacles = this.gameForm.get('obstacle').value;
      const nbOfRound = this.gameForm.get('nbOfRound').value;
      const speed = this.gameForm.get('speedForm').value;
      const liveGame = this.gameForm.get('liveGame').value;
      let game: Game = new Game();
      if (nbLine < 5 || nbLine > 25) {
        rejects(
          new Error('Le nombre de ligne doit être compris entre 5 et 25')
        );
      } else {
        game.mapHeight = nbLine;
      }
      if (nbColumn < 5 || nbColumn > 35) {
        rejects(
          new Error('Le nombre de colonne doit être compris entre 5 et 35')
        );
      } else {
        game.mapWidth = nbColumn;
      }
      if (nbCharacteres <= 0 || nbCharacteres >= 10) {
        rejects(
          new Error('Le nombre de personnage doit être compris entre 0 et 10')
        );
      } else {
        game.nbOfwizard = nbCharacteres;
      }
      if (obstacles > 10) {
        rejects(new Error("Le nombre d'obstacle est trop grand"));
      } else {
        game.nbObstacle = obstacles;
      }
      if (nbOfRound < 50 || nbOfRound > 1000) {
        rejects(
          new Error('Le nombre de round doit être compris entre 50 et 1000')
        );
      } else {
        game.nbOfRound = nbOfRound;
      }
      console.log('speed', speed.speed);
      game.liveGame = liveGame;
      game.speed = speed.speed;
      resolve(game);
    });
  }
  initGame(game: Game) {
    return new Promise<Game>(async (resolve, reject) => {
      const gameStatus = await this.http
        .get<boolean>(AppComponent.url + '/game/status')
        .toPromise();
      const gameIsInit = !gameStatus
        ? await this.http
            .post<boolean>(AppComponent.url + '/game/init', game, httpOptions)
            .pipe(catchError(this.handleError))
            .toPromise()
        : false;
      gameIsInit
        ? resolve(game)
        : reject(
            new Error('Initialistion est ratée, une partie est déjà en cours !')
          );
    });
  }
  closeAlert() {
    this.error = '';
  }
  waitForWinner() {
    const inter = interval(230).subscribe(() => {
      this.http
        .get<boolean>(AppComponent.url + '/game/status')
        .subscribe((status) => {
          if (!status) {
            inter.unsubscribe();
            this.http
              .get<any>(AppComponent.url + '/game/winner')
              .subscribe((winner) => {
                console.warn(winner);
                this.router.navigate(['history']);
              });
          }
        });
    });
  }
}
