import { Component, OnInit } from '@angular/core';
import {RoomsService} from './rooms.service';
import {Room} from '../types';
import {MatDialog} from '@angular/material/dialog';
import {RoomComponent} from './room.component';
import {YesNoComponent} from '../yesno.component';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css']
})
export class RoomsComponent implements OnInit {
  list: Array<Room> = [];

  constructor(
    private service: RoomsService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    this.service.list$().subscribe( items => this.list = items );
  }

  create(): void {
    const dialogRef = this.dialog.open(RoomComponent, {
      data: null
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result !== null) {
        this.service.create(result).subscribe( _ => this.reload() );
      }
    });
  }

  edit(room: Room): void {
    const dialogRef = this.dialog.open(RoomComponent, {
      data: room
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result !== null) {
        this.service.update(room, result).subscribe( _ => this.reload() );
      }
    });
  }

  lock(room: Room): void {
    this.service.lock(room).subscribe( _ => this.reload() );
  }

  unlock(room: Room): void {
    this.service.unlock(room).subscribe( _ => this.reload() );
  }

  delete(room: Room): void {
    const dialogRef = this.dialog.open(YesNoComponent, {
      data: {
        title: 'Wirklich löschen?',
        message: 'Sind Sie sicher, dass Sie den Raum ' + room.name + ' wirklich löschen wollen?'
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.service.delete(room, result).subscribe( _ => this.reload() );
      }
    });
  }
}
