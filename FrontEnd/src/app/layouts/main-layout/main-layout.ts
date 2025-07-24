import { Component } from '@angular/core';
import { NavbarComponent } from '../../navbar/navbar';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [NavbarComponent, RouterOutlet],
  templateUrl: './main-layout.html',
  styleUrls: ['./main-layout.css']

})
export class MainLayoutComponent {}
