package nl.tim.aoc.day15;

import nl.tim.aoc.Challenge;
import nl.tim.aoc.Main;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day15Challenge1 extends Challenge
{
    private int[] field;
    private List<Entity> entities;
    private int width;
    private int height;

    private final int WALL = 1;
    private final int EMPTY = 2;
    private final int ELF = 3;
    private final int GOBLIN = 4;
    private boolean elfKilled;

    private List<String> read;

    @Override
    public void prepare()
    {
        read = Main.readFile("15.txt");
        width = read.get(0).length();
        height = read.size();
        elfKilled = false;

        field = new int[width * height];
        entities = new ArrayList<>(25); // Seems like a reasonable amount of entities to expect
    }

    @Override
    public Object run(String alternative)
    {
        return run(false, 3);
    }

    protected String run(boolean powerCheck, int start)
    {
        int rounds = 0;
        int elfCount = amountOfElfs();
        int elfPower = start;

        for (; ; elfPower++)
        {
            load(elfPower);

            main:
            while (true)
            {
                //printRound(rounds);

                LinkedList<Entity> entities = getEntitiesThisRound();

                for (Entity entity : entities)
                {
                    if (entity.isAlive())
                    {
                        if (done())
                        {
                            break main;
                        }

                        tickEntity(entity);
                    }

                    // Check if elf was killed
                    if (powerCheck && elfKilled)
                    {
                        break main;
                    }
                }

                rounds += 1;
            }

            // Break if this is not a power check, or if it is and no elves have died
            if (!powerCheck || elfCount == amountOfElfs())
            {
                break;
            }

            rounds = 0;
        }

        printRound(-1);

        return rounds + " rounds and " + (rounds * sumHP() + " score") + " with " + elfPower + " attack power";
    }

    private void load(int attackPower)
    {
        entities.clear();

        for (int y = 0; y < height; y++)
        {
            String s = read.get(y);

            horRead:
            for (int x = 0; x < width; x++)
            {
                String charAt = s.substring(x, x + 1);

                switch (charAt)
                {
                    case "#": // Wall
                        field[x + y * width] = WALL;
                        break;
                    case "G": // Goblin
                        field[x + y * width] = GOBLIN;
                        entities.add(new Goblin(new Point(x, y)));
                        break;
                    case "E": // Elf
                        field[x + y * width] = ELF;
                        entities.add(new Elf(new Point(x, y), attackPower));
                        break;
                    case ".": // Empty
                        field[x + y * width] = EMPTY;
                        break;
                    default: // Whitespace or something -> end of field
                        break horRead;
                }
            }
        }
    }

    public void printRound(int i)
    {
        System.out.println("Round " + i + ":");
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                String res;
                int val = field[x + y * width];
                res = val == WALL ? "#" : val == EMPTY ? "." : val == ELF ? "E" : "G";

                System.out.print(res);
            }

            System.out.print("\n");
        }

        System.out.print("\n");
        for (Entity entity : entities)
        {
            System.out.println((entity instanceof Elf ? "Elf" : "Goblin") + " at " + entity.loc.x + "," + entity.loc.y + ": " + entity.hp);
        }
        System.out.print("\n\n");
    }

    private int amountOfElfs()
    {
        int count = 0;

        for (Entity entity : entities)
        {
            if (entity instanceof Elf)
            {
                count += 1;
            }
        }

        return count;
    }

    private int sumHP()
    {
        int sum = 0;

        for (Entity entity : entities)
        {
            sum += entity.isAlive() ? entity.hp : 0;
        }

        return sum;
    }

    private LinkedList<Entity> getEntitiesThisRound()
    {
        LinkedList<Entity> result = new LinkedList<>();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                Entity entity = getEntityAtLoc(new Point(x, y));

                if (entity != null && entity.isAlive())
                {
                    result.addLast(entity);
                }
            }
        }

        return result;
    }

    private boolean done()
    {
        boolean elfsPresent = false;
        boolean goblinsPresent = false;

        for (Entity entity : entities)
        {
            if (entity instanceof Elf)
            {
                elfsPresent = true;
            } else if (entity instanceof Goblin)
            {
                goblinsPresent = true;
            }

            if (elfsPresent && goblinsPresent)
            {
                return false;
            }
        }

        return true;
    }

    private void tickEntity(Entity entity)
    {
        // Check if this entity attacked someone
        if (!tryAttack(entity))
        {
            Point bestMove = getMovePoint(entity.loc, entity instanceof Elf ? GOBLIN : ELF);

            // Check if the turn ends
            if (bestMove == null)
            {
                return;
            }

            // Move
            moveEntity(entity, bestMove);

            // Try attack again
            tryAttack(entity);
        }
    }

    private boolean tryAttack(Entity entity)
    {
        Entity targetInRange = getEntityInRange(entity);

        if (targetInRange != null)
        {
            attackEntity(entity, targetInRange);

            return true;
        }

        return false;
    }

    private void attackEntity(Entity attacker, Entity target)
    {
        target.hp -= attacker.power;

        if (target.hp <= 0)
        {
            removeEntity(target);
        }
    }

    private void removeEntity(Entity entity)
    {
        // Reset field
        field[entity.loc.x + entity.loc.y * width] = EMPTY;

        // Remove from list
        entities.remove(entity);

        // Set elf flag
        if (entity instanceof Elf)
        {
            elfKilled = true;
        }
    }

    // I do not check the validity of moves here, pathfinder should handle that
    private void moveEntity(Entity entity, Point moveTo)
    {
        // First reset field
        field[entity.loc.x + entity.loc.y * width] = EMPTY;

        // Then set new field
        field[moveTo.x + moveTo.y * width] = entity instanceof Elf ? ELF : GOBLIN;

        // Then change location in entity
        entity.loc.setLocation(moveTo);
    }

    private Entity getEntityInRange(Entity entity)
    {
        Entity bestTarget = null;

        for (Point point : entity.getSurroundingLocations())
        {
            Entity target = getEntityAtLoc(point);

            if (target != null && entity instanceof Elf ? target instanceof Goblin : target instanceof Elf)
            {
                if (bestTarget == null || target.hp < bestTarget.hp)
                {
                    bestTarget = target;
                }

                if (target.hp == bestTarget.hp &&
                        (target.loc.y < bestTarget.loc.y ||
                                (target.loc.y == bestTarget.loc.y && target.loc.x < bestTarget.loc.x)))
                {
                    bestTarget = target;
                }
            }
        }

        return bestTarget;
    }

    private Entity getEntityAtLoc(Point loc)
    {
        for (Entity entity : entities)
        {
            if (entity.loc.equals(loc))
            {
                return entity;
            }
        }

        return null;
    }

    private boolean isWalkable(int x, int y)
    {
        return x > 0 && x < width && y > 0 && y < height && field[x + y * width] == EMPTY;
    }


    private Point getMovePoint(Point start, int target)
    {
        LinkedList<NodePoint> result = null;
        Point p = null;

        for (Entity entity : entities)
        {
            if ((target == ELF && entity instanceof Elf) ||
                    (target == GOBLIN && entity instanceof Goblin))
            {
                for (Point point : entity.getSurroundingLocations())
                {
                    LinkedList<NodePoint> path = getPath(start, point);

                    // Only proceed if path exists
                    if (path != null)
                    {
                        if (result == null ||
                                path.size() < result.size())
                        {
                            result = path;
                            p = point;
                            continue;
                        }

                        if (path.size() == result.size())
                        {
                            Point lastStep = path.getLast();
                            Point lastStepOld = result.getLast();

                            if (lastStep.y < lastStepOld.y ||
                                    (lastStep.y == lastStepOld.y && lastStep.x < lastStepOld.x))
                            {
                                result = path;
                                p = point;
                            }
                        }
                    }
                }
            }
        }

        result = getPath(start, p);

        return result == null ? null : result.getFirst();
    }

    /*
    Slightly modified (read ruined because of weird movement rules) version of A*
     */
    private LinkedList<NodePoint> getPath(Point start, Point target)
    {
        if (target == null || !isWalkable(target.x, target.y))
        {
            return null;
        }

        LinkedList<NodePoint> bestRoute = null;

        for (Point pS : new Entity(start).getSurroundingLocations())
        {
            Set<NodePoint> open = new HashSet<>();
            LinkedList<NodePoint> closed = new LinkedList<>();
            NodePoint startNode = new NodePoint(null, pS.x, pS.y);

            startNode.cost = 0;
            startNode.heur = 0;
            startNode.dist = 0;

            // Not a viable route
            if (!isWalkable(pS.x, pS.y))
            {
                continue;
            }

            open.add(startNode);

            while (!open.isEmpty())
            {
                NodePoint currentNode = null;

                // Get node with lowest cost
                for (NodePoint node : open)
                {
                    if (currentNode == null || node.cost < currentNode.cost)
                    {
                        currentNode = node;
                    }

                    // Check if cost is the same
                    // If so use the weird AoC ordering-rule
                    if (node.cost == currentNode.cost && node.dist == currentNode.dist)
                    {
                        if (node.y < currentNode.y || (node.y == currentNode.y && node.x < currentNode.x))
                        {
                            currentNode = node;
                        }
                    }
                }

                // Switch lists
                open.remove(currentNode);
                closed.addLast(currentNode);

                // Check if we found target
                if (currentNode.x == target.x && currentNode.y == target.y)
                {
                    LinkedList<NodePoint> res = new LinkedList<>();
                    NodePoint cur = currentNode;

                    do
                    {
                        res.addFirst(cur);

                        cur = cur.parent;

                    } while (cur != null);

                    if (bestRoute == null || bestRoute.size() > res.size())
                    {
                        bestRoute = res;
                    } else if (bestRoute.size() == res.size())
                    {
                        NodePoint firstBest = bestRoute.getFirst();

                        if (pS.y < firstBest.y || (pS.y == firstBest.y && pS.x < firstBest.x))
                        {
                            bestRoute = res;
                        }
                    }

                    break;
                }

                // Since our entities cannot walk diagonally skip that step
                for (int i = 0; i < 4; i++)
                {
                    int x = currentNode.x + (i == 0 ? 1 : i == 1 ? -1 : 0);
                    int y = currentNode.y + (i == 2 ? 1 : i == 3 ? -1 : 0);
                    NodePoint node = new NodePoint(currentNode, x, y);

                    // Check if this is a valid node
                    if (getNode(closed, x, y) != null || !isWalkable(x, y))
                    {
                        continue;
                    }

                    NodePoint nodeInSet = getNode(open, x, y);
                    node.dist = currentNode.dist + 1;
                    node.heur = Math.abs(node.x - target.x) +
                            Math.abs(node.y - target.y);
                    node.cost = node.dist + node.heur;

                    // Check if node is in set
                    if (nodeInSet != null)
                    {
                        // Check if this node is a better path
                        if (node.dist < nodeInSet.dist)
                        {
                            open.remove(nodeInSet);
                            open.add(node);
                        }

                        // Check if distance is the same
                        // If so use the weird AoC ordering-rule
                        if (node.dist == nodeInSet.dist)
                        {
                            if (node.y < nodeInSet.y || (node.y == nodeInSet.y && node.x < nodeInSet.x))
                            {
                                open.remove(nodeInSet);
                                open.add(node);
                            }
                        }
                    } else
                    {
                        open.add(node);
                    }
                }
            }
        }

        return bestRoute;
    }

    private NodePoint getNode(Collection<NodePoint> in, int x, int y)
    {
        if (in != null)
        {
            for (NodePoint node : in)
            {
                if (node.x == x && node.y == y)
                {
                    return node;
                }
            }
        }
        return null;
    }

    public class NodePoint extends Point
    {
        private int dist;
        private int heur;
        private int cost;
        private NodePoint parent;

        public NodePoint(NodePoint parent, int x, int y)
        {
            super(x, y);

            this.parent = parent;
        }

        // Needed for A*
        @Override
        public boolean equals(Object object)
        {
            if (object == this)
            {
                return true;
            }

            if (!(object instanceof NodePoint))
            {
                return false;
            }

            NodePoint node = (NodePoint) object;

            return node.x == x &&
                    node.y == y &&
                    node.cost == cost &&
                    node.dist == dist &&
                    node.heur == heur &&
                    node.parent == parent;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(x, y, cost, dist, heur, parent);
        }
    }

    public class Entity
    {
        private int hp;
        protected int power;
        private Point loc;

        public Entity(Point loc)
        {
            this.hp = 200;
            this.power = 3;
            this.loc = loc;
        }

        @Override
        public boolean equals(Object object)
        {
            if (object == this)
            {
                return true;
            }

            if (!(object instanceof Entity))
            {
                return false;
            }

            Entity entity = (Entity) object;

            return entity.loc.equals(loc) && entity.hp == hp && entity.power == power;
        }

        private boolean isAlive()
        {
            return hp > 0;
        }

        private Point[] getSurroundingLocations()
        {
            return new Point[]{new Point(loc.x + 1, loc.y),
                    new Point(loc.x - 1, loc.y),
                    new Point(loc.x, loc.y + 1),
                    new Point(loc.x, loc.y - 1)
            };
        }
    }

    public class Goblin extends Entity
    {

        private Goblin(Point loc)
        {
            super(loc);
        }
    }

    public class Elf extends Entity
    {
        public Elf(Point loc, int power)
        {
            super(loc);

            this.power = power;
        }
    }
}
