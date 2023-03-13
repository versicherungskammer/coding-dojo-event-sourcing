export interface RoomData {
  name: string;
  maintenance: boolean;
}
export interface Room extends RoomData {
  id: string;
}

export interface PersonData {
  username: string;
  fullname: string;
  sick: boolean;
}
export interface Person extends PersonData {
  id: string;
}

export interface ReservationPersonData {
  id: string;
  username: string;
  fullname: string;
}
export interface ReservationRoomData {
  id: string;
  name: string;
}
export interface Reservation {
  id: string;
  room: ReservationRoomData;
  person: ReservationPersonData;
}
export interface ReservationCreateData {
  room: string;
  person: string;
}

export interface Feedback {
  type: string;
}
export interface SuccessFeedback extends Feedback {
  type: 'success';
  reference: string;
}
export interface FailFeedback extends Feedback {
  type: 'error';
  error: string;
}
export interface WaitFeedback extends Feedback {
  type: 'unknown';
}
