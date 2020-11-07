import { ActivatedRoute, Router, RoutesRecognized } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'HarryPotterGame';
  url: string;
  static readonly url = 'api';
  constructor(private router: Router, private route: ActivatedRoute) {}
  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof RoutesRecognized) {
        this.url = event.url;
      }
    });
    this.router.navigate(['/config']);
  }
}
