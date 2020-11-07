import { Box } from '../box/box';

export interface Map{
    id: number;
    height:number;
    width:number;
    zone:Box[][];    
}