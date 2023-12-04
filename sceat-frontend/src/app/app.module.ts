import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { KitchenComponent } from './kitchen/kitchen.component';
import { SchoolAdministrativeComponent } from './school-administrative/school-administrative.component';
import { StatisticsViewComponent } from './statistics-view/statistics-view.component';
import { PictureHolderComponent } from './kitchen/picture-holder/picture-holder.component';
import { DescriptionComponent } from './kitchen/description/description.component';
import { InfoHolderComponent } from './kitchen/info-holder/info-holder.component';
import { MatCardModule } from "@angular/material/card";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    KitchenComponent,
    SchoolAdministrativeComponent,
    StatisticsViewComponent,
    PictureHolderComponent,
    DescriptionComponent,
    InfoHolderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
      BrowserAnimationsModule,
      MatCardModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
