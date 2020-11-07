import { Box } from './../box/box';
export interface Wizard {
  id: number;
  master:boolean;
  status: string;
  name: string;
  currentBox: Box;
  house: any;
  messages: any;
  wasAttack: boolean;
  energiePoints: number;
  dementorAttacked: boolean;
  //picUrl: string;
}
