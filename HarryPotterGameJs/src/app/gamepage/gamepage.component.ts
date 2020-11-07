import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription, interval } from 'rxjs';

import { AppComponent } from '../app.component';
import { Dementor } from './../dementor/dementor';
import { HttpClient } from '@angular/common/http';
import { MovableService } from '../wizard/movable.service';
import { ThrowStmt } from '@angular/compiler';
import { Wizard } from '../wizard/wizard';

@Component({
  selector: 'app-gamepage',
  templateUrl: './gamepage.component.html',
  styleUrls: ['./gamepage.component.css'],
})
export class GamepageComponent implements OnInit, OnDestroy {
  round: number = 0;
  roundMax: number = 500;
  isGameStarted: boolean;
  speed: number;
  winner: any;
  wizardList: Wizard[];
  dementorList: Dementor[];
  interval: Subscription;
  constructor(
    private http: HttpClient,
    private movableservice: MovableService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  ngOnDestroy(): void {
    this.stop();
  }
  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      this.speed = Number(params.get('speed'));
      this.roundMax = Number(params.get('nbOfRound'));
    });
    this.http
      .get<boolean>(AppComponent.url + '/game/init/status')
      .subscribe((bool) => {
        bool ? this.init() : this.gotoConfigPage();
      });
  }
  start() {
    this.http
      .get<boolean>(AppComponent.url + '/game/start')
      .subscribe((bool) => {
        this.isGameStarted = bool;
      });
    this.interval = interval(this.speed).subscribe(() => {
      this.http
        .get<boolean>(AppComponent.url + '/game/status')
        .subscribe(async (status) => {
          this.getWizards();
          this.getDementors();
          if (status) {
            this.round = await this.getRound();
          } else {
            this.isGameStarted = false;
            this.winner = await this.getWinner();
            console.warn(this.winner);
            this.interval.unsubscribe();
          }
        });
    });
  }
  stop() {
    this.http
      .get<boolean>(AppComponent.url + '/game/stop')
      .subscribe((bool) => {
        if (bool) {
          this.isGameStarted = !bool;
          if (this.interval !== undefined) {
            this.interval.unsubscribe();
          }
        }
      });
  }
  init() {
    this.movableservice.currentWizard.subscribe(
      (wizards) => (this.wizardList = wizards)
    );
    this.movableservice.currentDementors.subscribe(
      (dementors) => (this.dementorList = dementors)
    );
    this.getGameStatus();
    this.getWizards();
    this.getDementors();
  }
  gotoConfigPage() {
    this.router.navigate(['']);
  }
  getWizards() {
    this.http
      .get<Array<Wizard>>(AppComponent.url + '/game/wizards')
      .subscribe((wizards) => {
        this.movableservice.setWizard(wizards);
      });
  }
  getDementors() {
    this.http
      .get<Dementor[]>(AppComponent.url + '/game/dementors')
      .subscribe((dementors) => {
        this.movableservice.setDementors(dementors);
      });
  }
  getGameStatus() {
    this.http
      .get<boolean>(AppComponent.url + '/game/status')
      .subscribe((bool) => {
        if (bool) {
          this.stop();
        }
      });
  }
  getWinner() {
    return this.http.get<any>(AppComponent.url + '/game/winner').toPromise();
  }
  getRound() {
    return this.http.get<number>(AppComponent.url + '/game/round').toPromise();
  }
}
