import { Component, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLinkWithHref, ActivatedRoute, isActive, Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from './services/auth-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLinkWithHref],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private authService = inject(AuthService);
  private activatedRoute = inject(ActivatedRoute);
  private router = inject(Router);
  route = signal<String | null>(null);

  ngOnInit() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.route.set(event.urlAfterRedirects)
    });
  }

  inPrivateRoutes = () => {
    let publicRoutes = ['/', '/register']
    return !publicRoutes.some(val => this.route() == val)
  }

  logout = () => {
    this.authService.unauthenticate();
  }
}
