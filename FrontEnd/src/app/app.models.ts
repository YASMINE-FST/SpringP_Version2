
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module'; // <-- ici, on importe le vrai module
import { AppComponent } from './appComponent/app.component';

@NgModule({
  declarations: [],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,  // <-- module de routing importé
    AppComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
