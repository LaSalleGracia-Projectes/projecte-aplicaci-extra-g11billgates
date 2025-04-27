<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Chat extends Model
{
    use HasFactory;

    public $timestamps = false;

    protected $primaryKey = 'IDChat';


    protected $fillable = [
        'IDChat',
        'IDMatch',
        'FechaCreacion'
    ];
    //relacion a matchusers 1:1
    public function matchUsers()
    {
        return $this->hasOne(MatchUsers::class);
    }
    //relacion a mensaje 1:n
    public function mensaje()
    {
        return $this->hasMany(Mensaje::class);
    }
}
