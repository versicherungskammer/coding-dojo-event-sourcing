import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTabsModule} from '@angular/material/tabs';
import {HttpClientModule} from '@angular/common/http';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import {YesNoComponent} from './yesno.component';
import {MatDialogModule} from '@angular/material/dialog';
import {PersonsComponent} from './persons/persons.component';
import {PersonComponent} from './persons/person.component';
import {MatSelectModule} from '@angular/material/select';
import {RoomsComponent} from './rooms/rooms.component';
import {RoomComponent} from './rooms/room.component';
import {ReservationComponent} from './reservations/reservation.component';
import {ReservationsComponent} from './reservations/reservations.component';

@NgModule({
  declarations: [
    AppComponent,
    YesNoComponent,
    RoomsComponent,
    RoomComponent,
    PersonsComponent,
    PersonComponent,
    ReservationsComponent,
    ReservationComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatDialogModule,
    MatTabsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatSelectModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
